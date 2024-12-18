package com.verycoolprojects.youtubeapp.mapper;

import com.verycoolprojects.youtubeapp.dto.search.HistoryItem;
import com.verycoolprojects.youtubeapp.dto.search.HistoryResponse;
import com.verycoolprojects.youtubeapp.dto.search.VideoQuery;
import com.verycoolprojects.youtubeapp.entity.History;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class HistoryMapper {

    public HistoryResponse map(List<History> historyList) {
        return HistoryResponse.builder()
                .items(historyList.stream()
                        .map(item -> HistoryItem.builder()
                                .id(item.getId())
                                .date(new Date(item.getRequestDate().getTime()))
                                .query(VideoQuery.builder()
                                        .q(item.getQ())
                                        .publishedAfter(item.getPublishedAfter())
                                        .publishedBefore(item.getPublishedBefore())
                                        .category(item.getCategory())
                                        .locationData(item.getLocationData())
                                        .order(item.getOrder())
                                        .build())
                                .build()).collect(Collectors.toList())).build();
    }

}
