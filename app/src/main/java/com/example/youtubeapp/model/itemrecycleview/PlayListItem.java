package com.example.youtubeapp.model.itemrecycleview;

import java.io.Serializable;

public class PlayListItem implements Serializable {
    private String urlThumbnailsVideo;
    private String videoCount;
    private String titleVideo;
    private String titleChannel;
    private String idPlayList;

    public PlayListItem(String urlThumbnailsVideo, String videoCount, String titleVideo, String titleChannel, String idPlayList) {
        this.urlThumbnailsVideo = urlThumbnailsVideo;
        this.videoCount = videoCount;
        this.titleVideo = titleVideo;
        this.titleChannel = titleChannel;
        this.idPlayList = idPlayList;
    }

    public String getIdPlayList() {
        return idPlayList;
    }

    public void setIdPlayList(String idPlayList) {
        this.idPlayList = idPlayList;
    }

    public String getUrlThumbnailsVideo() {
        return urlThumbnailsVideo;
    }

    public void setUrlThumbnailsVideo(String urlThumbnailsVideo) {
        this.urlThumbnailsVideo = urlThumbnailsVideo;
    }

    public String getVideoCount() {
        return videoCount;
    }

    public void setVideoCount(String videoCount) {
        this.videoCount = videoCount;
    }

    public String getTitleVideo() {
        return titleVideo;
    }

    public void setTitleVideo(String titleVideo) {
        this.titleVideo = titleVideo;
    }

    public String getTitleChannel() {
        return titleChannel;
    }

    public void setTitleChannel(String titleChannel) {
        this.titleChannel = titleChannel;
    }
}
