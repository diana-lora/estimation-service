package com.sellics.estimationservice.client;

import com.sellics.estimationservice.config.AmazonAutocompleteConfig;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.AllArgsConstructor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "amazon-autocomplete-api", url = "${client.amazon.autocomplete.api}", configuration = AmazonApi.AutocompleteInterceptor.class)
public interface AmazonApi {

    @GetMapping("/search/complete")
    String complete(@RequestParam String q);

    @AllArgsConstructor
    class AutocompleteInterceptor implements RequestInterceptor {

        private final AmazonAutocompleteConfig amazonAutocompleteConfig;

        @Override
        public void apply(RequestTemplate requestTemplate) {
            requestTemplate.query("search-alias", amazonAutocompleteConfig.getSearchAlias());
            requestTemplate.query("client", amazonAutocompleteConfig.getClient());
            requestTemplate.query("mkt", amazonAutocompleteConfig.getMkt().toString());
        }
    }
}
