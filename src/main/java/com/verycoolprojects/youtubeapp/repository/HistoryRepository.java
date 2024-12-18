package com.verycoolprojects.youtubeapp.repository;

import com.verycoolprojects.youtubeapp.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<History, Integer> {
    List<History> findByUsernameOrderByRequestDateDesc(String username);
}
