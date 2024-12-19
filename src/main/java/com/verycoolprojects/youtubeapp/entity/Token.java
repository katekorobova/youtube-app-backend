package com.verycoolprojects.youtubeapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "token", schema = "authentication")
public class Token {
    @Id
    private String id;

    @Column(nullable = false)
    private String username;

    @Column(name = "issued_at")
    private Timestamp issuedAt;

    @Column(name = "expires_at")
    private Timestamp expiresAt;

    private boolean revoked;
}
