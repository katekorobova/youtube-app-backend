package com.verycoolprojects.youtubeapp.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SearchResultThumbnails {
    @JsonProperty("default")
    public SearchResultThumbnail defaultThumbnail;
    public SearchResultThumbnail medium;
    public SearchResultThumbnail high;
}
