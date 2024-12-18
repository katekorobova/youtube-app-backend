package com.verycoolprojects.youtubeapp.client.model;

import lombok.Data;

import java.util.Date;

@Data
public class SearchResultSnippet {
    public Date publishedAt;
    public String channelId;
    public String title;
    public String description;
    public SearchResultThumbnails thumbnails;
    public String channelTitle;
    public String liveBroadcastContent;
    public Date publishTime;
}
