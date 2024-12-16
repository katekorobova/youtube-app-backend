package com.verycoolprojects.youtubeapp.controller;

import com.verycoolprojects.youtubeapp.model.history.History;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@CrossOrigin
@RestController
public class HistoryController {

    @GetMapping("/history")
    public ResponseEntity<History> getHistory() {
        log.debug("History Request: ...");
        History response = new History();
        log.debug("History response: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
