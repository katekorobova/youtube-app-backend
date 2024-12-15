package com.verycoolprojects.youtubeapp.client;

import com.verycoolprojects.youtubeapp.model.SearchResult;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
public class YoutubeDataClient {
    public final static String SEARCH_URL = "/search";

    private final RestClient restClient;

    private final String apiKey;

    public YoutubeDataClient(RestClient restClient, @Value("${client.apiKey}") String apiKey) {
        this.restClient = restClient;
        this.apiKey = apiKey;
    }

    @SneakyThrows
    public SearchResult search(MultiValueMap<String, String> queryParams) {
        queryParams.add("key", apiKey);
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(SEARCH_URL)
                        .queryParams(queryParams)
                        .build())
                .retrieve()
                .body(SearchResult.class);
    }
}
