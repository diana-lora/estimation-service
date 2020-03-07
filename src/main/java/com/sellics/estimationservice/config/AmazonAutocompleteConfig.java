package com.sellics.estimationservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "client.amazon.autocomplete.api")
public class AmazonAutocompleteConfig {
    private String searchAlias;
    private String client;
    private Integer mkt;
}
