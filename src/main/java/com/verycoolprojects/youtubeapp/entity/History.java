package com.verycoolprojects.youtubeapp.entity;

import com.verycoolprojects.youtubeapp.dto.search.LocationData;
import com.verycoolprojects.youtubeapp.dto.search.VideoQuery;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Entity
@NoArgsConstructor
@Table(name = "history")
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String username;

    @Column(name = "request_date")
    private Timestamp requestDate;

    private String q;

    @Column(name = "published_after")
    private String publishedAfter;

    @Column(name = "published_before")
    private String publishedBefore;

    private String category;

    @Column(name = "location")
    private LocationData locationData;

    @Column(name = "result_order")
    private String order;

    public History(String username, VideoQuery videoQuery) {
        this.username = username;
        this.requestDate = new Timestamp(System.currentTimeMillis());

        this.q = videoQuery.getQ();
        this.order = videoQuery.getOrder();
        this.publishedAfter = videoQuery.getPublishedAfter();
        this.publishedBefore = videoQuery.getPublishedBefore();
        this.locationData = videoQuery.getLocationData();
        this.category = videoQuery.getCategory();
    }
}
