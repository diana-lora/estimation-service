package com.sellics.estimationservice.web.dto;

import lombok.Value;

@Value
public class KeywordSearchVolume {
    private String keyword;
    private Integer score;
}
