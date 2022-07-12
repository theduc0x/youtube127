package com.example.youtubeapp.model.itemrecycleview;

public class VideoChannelItem {
    private String urlThumbnails;
    private String titleVideo;
    private String publishAt;
    private String viewCount;
    private String idVideo;

    public VideoChannelItem(String urlThumbnails, String titleChannel, String publishAt, String viewCount, String idVideo) {
        this.urlThumbnails = urlThumbnails;
        this.titleVideo = titleChannel;
        this.publishAt = publishAt;
        this.viewCount = viewCount;
        this.idVideo = idVideo;
    }

    public String getUrlThumbnails() {
        return urlThumbnails;
    }

    public void setUrlThumbnails(String urlThumbnails) {
        this.urlThumbnails = urlThumbnails;
    }

    public String getTitleVideo() {
        return titleVideo;
    }

    public void setTitleVideo(String titleVideo) {
        this.titleVideo = titleVideo;
    }

    public String getPublishAt() {
        return publishAt;
    }

    public void setPublishAt(String publishAt) {
        this.publishAt = publishAt;
    }

    public String getViewCount() {
        return viewCount;
    }

    public void setViewCount(String viewCount) {
        this.viewCount = viewCount;
    }

    public String getIdVideo() {
        return idVideo;
    }

    public void setIdVideo(String idVideo) {
        this.idVideo = idVideo;
    }
}
