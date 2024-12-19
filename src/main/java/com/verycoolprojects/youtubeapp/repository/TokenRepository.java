package com.verycoolprojects.youtubeapp.repository;

import com.verycoolprojects.youtubeapp.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, String> {

}
