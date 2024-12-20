package com.verycoolprojects.youtubeapp.dto.search;

import lombok.Builder;
import lombok.Data;

import java.util.Date;


@Data
@Builder
public class HistoryItem {
    private long id;
    private Date date;
    private VideoQuery query;
}
