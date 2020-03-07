package com.sellics.estimationservice.web;

import com.sellics.estimationservice.service.SearchVolumeService;
import com.sellics.estimationservice.web.dto.KeywordSearchVolume;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class SearchVolumeController {

    private final SearchVolumeService keywordEstimationService;

    @GetMapping("/estimate")
    public KeywordSearchVolume getKeywordEstimation(@RequestParam @NotNull String keyword) {
        return new KeywordSearchVolume(keyword, keywordEstimationService.estimate(keyword));
    }

}
