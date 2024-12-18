package com.verycoolprojects.youtubeapp.client.model;

import lombok.Data;

import java.util.List;

@Data
public class SearchResult {
    public String kind;
    public String etag;
    public String nextPageToken;
    public String regionCode;
    public SearchResultPageInfo pageInfo;
    public List<SearchResultItem> items;
}
