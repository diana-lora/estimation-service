# Estimation Service

## Requirements

For the assignment, you have to implement a microservice with a single REST endpoint.
This endpoint should receive a single keyword as an input and should return a score for that
same exact keyword. The score should be in the range [0 → 100] and represent the
estimated search-volume (how often Amazon customers search for that exact keyword). A
score of 0 means that the keyword is practically never searched for, 100 means that this is
one of the hottest keywords in all of amazon.com right now.

**Example API Request**
```
REQUEST GET http://localhost:8080/estimate?keyword=iphone+charger
RESPONSE
{
   “Keyword”:”iphone charger”,
   “score”:87
}
```

## Assumptions
1. For any search input, Amazon will only return up to 10 keywords, that have an exact
prefix-match with the input.
1. Any keyword with a relevant search-volume can be returned by the API.
1. Whenever the API is called, it operates in 2 steps:
   1. Seek: Get all known keywords that match the prefix and create a
Candidate-Set
   1. Sort/Return: Sort the Candidate-Set by search-volume and return the top 10
results.
1. *hint: the order of the 10 returned keywords is comparatively insignificant!

## Documentation

### What assumptions did you make?
1. All words in `keyword` have the same weight.
1. `score` attribute should be `Integer`. So, I did a round up approach to convert from decimal to integer.
1. The order of the 10 returned keywords is insignificant.

### How does your algorithm work?

The algorithm calculates the score of each word in `keyword` separately and then does a pondered summation, where all
words have equal weight. 

A word score calculation is computed based on a subset of strings of the complete word.
A word score is defined as the summation of the pondered word occurrences from the `substrings` autocomplete options. 
For each word, the Amazon Autocomplete endpoint is called <code>n</code> times, where <code>n</code> is the size of the word.
The first call, the <code>substring</code> value is the first letter of the word. The second call,
the <code>substring</code> value is the first two letters of the word. The process goes on until one of the following statements are true:
<ul>
<li><code>substring</code> is equal to the whole word.</li>
<li>The word appears in all autocomplete options from a <code>substring</code>.</li>
</ul>

Each <code>substring</code> is consider to have the same importance or weight (see <code>subStringWeight</code>). An occurrence
happens when the complete word appears in one element of the <code>substring</code> autocomplete options response.

#### Example

In the case of `iphone charger` the procedure is as follows. Each `substring` from iphone word has `16.6%` maximum weight. The following substrings are created:

| - | I | P | H | O | N | E |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: |
| substring | i | ip | iph | ipho | iphon | iphone |
| occurrences | 9 | 9 | 10 | 10 | 10 | 10 |
| score | 15 | 15 | 16.6 | 16.6 | 16.6 | 16.6 |

Iphone has a search volume estimation of `96,4`.

The same procedure repeats for charger word. Each `substring` from iphone word has `14.2%` maximum weight. 
The following substrings are created:

| - | C | H | A | R | G | E | R |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| substring | c | ch | cha | char | charg | charge | charger |
| occurrences | 0 | 0 | 0 | 0 | 2 | 4 | 10 |
| score | 0 | 0 | 0 | 0 | 2.84 | 5.68 | 14.2 |

Charger has a search volume estimation of `22.72`

All words are considered to have equal weight, so the total score is `score = 96.4 * 0.5 + 22.72 * 0.5 = 59.56`, which rounds up to `60`.

### Do you think the (*hint) that we gave you earlier is correct and if so - why?

Maybe between the options there is not a significant difference, but looks like those are the most common 
combinations that contain the word been searched.

### How precise do you think your outcome is and why?

I think it is good enough for a first step. But I would definitely consider a way on how to identify the weight of each word in `keyword`. 
This would impact the result significantly.

