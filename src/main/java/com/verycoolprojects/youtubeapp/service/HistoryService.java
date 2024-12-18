package com.verycoolprojects.youtubeapp.service;

import com.verycoolprojects.youtubeapp.entity.History;
import com.verycoolprojects.youtubeapp.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryService {
    private final HistoryRepository repository;

    public List<History> findByUsername(String username) {
        return repository.findByUsernameOrderByRequestDateDesc(username);
    }

    public History save(History history) {
        return repository.save(history);
    }
}
