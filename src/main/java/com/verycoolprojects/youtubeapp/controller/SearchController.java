package com.verycoolprojects.youtubeapp.controller;

import com.verycoolprojects.youtubeapp.client.YoutubeDataClient;
import com.verycoolprojects.youtubeapp.client.model.SearchResult;
import com.verycoolprojects.youtubeapp.dto.search.HistoryResponse;
import com.verycoolprojects.youtubeapp.dto.search.VideoQuery;
import com.verycoolprojects.youtubeapp.entity.History;
import com.verycoolprojects.youtubeapp.mapper.HistoryMapper;
import com.verycoolprojects.youtubeapp.service.HistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SearchController {
    private final YoutubeDataClient client;
    private final HistoryService service;
    private final HistoryMapper mapper;

    @PostMapping("/search")
    public ResponseEntity<SearchResult> search(Authentication authentication, @RequestBody VideoQuery videoQuery) {
        log.debug("Search Request: {}", videoQuery);

        if (Objects.nonNull(authentication)) {
            String username = authentication.getName();
            log.debug("Username: {}", username);
            new Thread(() -> service.save(new History(username, videoQuery))).start();
        }
        SearchResult response = client.search(videoQuery);
        log.debug("Search Response: {}", response);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/history")
    public ResponseEntity<HistoryResponse> getHistory(Authentication authentication) {
        String username = authentication.getName();
        log.debug("History Request: username={}", username);

        List<History> historyList = service.findByUsername(username);
        HistoryResponse response = mapper.map(historyList);

        log.debug("History response: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
