package com.verycoolprojects.youtubeapp.controller;

import com.verycoolprojects.youtubeapp.client.YoutubeDataClient;
import com.verycoolprojects.youtubeapp.model.SearchResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@CrossOrigin
@RestController
@RequiredArgsConstructor
public class SearchController {
    private final YoutubeDataClient client;

    @GetMapping("/search")
    public ResponseEntity<SearchResult> search(@RequestParam MultiValueMap<String, String> queryParams) {
        log.debug("Received a search request: {}", queryParams);
        SearchResult response = client.search(queryParams);
        log.debug("Response: {}", response);
        return ResponseEntity.ok(response);
    }
}
