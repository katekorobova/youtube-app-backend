package com.verycoolprojects.youtubeapp.dto.search;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VideoQuery {
    private String q;
    private String publishedBefore;
    private String publishedAfter;
    private String category;
    private LocationData locationData;
    private String order;
}
