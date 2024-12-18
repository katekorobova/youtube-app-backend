package com.verycoolprojects.youtubeapp.client;

import com.verycoolprojects.youtubeapp.client.model.SearchResult;
import com.verycoolprojects.youtubeapp.dto.search.VideoQuery;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.Objects;

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
    public SearchResult search(VideoQuery videoQuery) {
        MultiValueMap<String, String> queryParams = getQueryParams(videoQuery);
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(SEARCH_URL)
                        .queryParams(queryParams)
                        .build())
                .retrieve()
                .body(SearchResult.class);
    }

    private MultiValueMap<String, String> getQueryParams(VideoQuery videoQuery) {
        LinkedMultiValueMap<String, String> multiMap = new LinkedMultiValueMap<>();
        multiMap.add("key", apiKey);
        multiMap.add("part", "snippet");
        multiMap.add("type", "video");
        multiMap.add("maxResults", "50");
        multiMap.add("q", videoQuery.getQ());
        multiMap.add("publishedAfter", videoQuery.getPublishedAfter());
        multiMap.add("publishedBefore", videoQuery.getPublishedBefore());
        multiMap.add("category", videoQuery.getCategory());
        multiMap.add("order", videoQuery.getOrder());

        if (Objects.nonNull(videoQuery.getLocationData())) {
            multiMap.add("location", videoQuery.getLocationData().getLocation());
            multiMap.add("locationRadius", videoQuery.getLocationData().getLocationRadius());
        }

        return multiMap;
    }
}
