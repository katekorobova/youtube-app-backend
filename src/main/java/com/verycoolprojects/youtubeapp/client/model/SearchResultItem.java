package com.verycoolprojects.youtubeapp.client.model;

import lombok.Data;

@Data
public class SearchResultItem {
    public String kind;
    public String etag;
    public SearchResultId id;
    public SearchResultSnippet snippet;
}
