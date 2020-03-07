package com.sellics.estimationservice.service;

import com.sellics.estimationservice.client.AmazonApi;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SearchVolumeService {

    private final AmazonApi amazonAutocompleteApi;

    /**
     * Returns the summation of all word scores.
     *
     * Note: A round up approach is used to convert from decimal to integer.
     *
     * @param keyword
     * @return
     */
    public Integer estimate(String keyword) {
        List<String> words = getWords(keyword);
        // I will consider that all words have the same importance for simplicity purposes.
        Double weightPerWord = 1D/words.size();
        return (int) Math.round(words.stream().mapToDouble(word -> score(word) * weightPerWord).sum());
    }

    /**
     * Returns the score of a word.
     *
     * The word score calculation is computed based on a subset of strings that are contained inside the word.
     * For each word, the Amazon Autocomplete endpoint is called <code>n</code> times, where <code>n</code> is the size of the word.
     * The first call, the <code>substring</code> value is the first letter of the word. The second call,
     * the <code>substring</code> value is the first two letters of the word. The process goes on until one of the following statements is true:
     * <ul>
     *     <li>The complete word is sent</li>
     *     <li>The word appears in all autocomplete options from a <code>substring</code>.</li>
     * </ul>
     *
     * The <code>substring</code> score is calculated every time the Amazon Autocomplete endpoint is called. Each
     * <code>substring</code> is consider to have the same importance or weight (see <code>subStringWeight</code>). An occurrence
     * happens when the complete word appears in one element of the <code>substring</code> autocomplete options. A word score is defined as the
     * summation of the pondered word occurrences of all substrings contained in the word.
     *
     * @param word word to score
     * @return
     */
    private Double score(String word) {
        Integer n = word.length();
        Double subStringWeight = 100D/n;
        Double score = 0D;

        for (int i = 0; i <= n; i++) {
            String substring = word.substring(0, i + 1);
            List<String> autocomplete = getAmazonAutocomplete(substring);
            Long occurrences = autocomplete.stream().filter(s -> s.contains(word)).count();
            if (occurrences == autocomplete.size()) {
                score += subStringWeight + (n - (i + 1)) * subStringWeight;
                break;
            }
            score += subStringWeight * (occurrences/(double)autocomplete.size());
        }
        return score;
    }

    /**
     * Returns list of words in a <code>String</code>.
     *
     * @param keyword input <code>String</code>.
     * @return
     */
    private List<String> getWords(String keyword) {
        return Arrays.asList(keyword.trim().split("\\s+"));
    }

    /**
     * Returns parsed options from the autocomplete amazon search.
     *
     * @param keyword input keyword search.
     * @return
     */
    private List<String> getAmazonAutocomplete(String keyword) {
        String response = amazonAutocompleteApi.complete(keyword);
        String[] split = response.replaceAll("\\[\\\".*\\\",\\[","").replaceAll("\\],\\[.*?\\\"\\]","").split(",");
        return Arrays.asList(split);
    }
}
