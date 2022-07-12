package com.example.youtubeapp.model.itemrecycleview;

public class ItemVideoInPlayList {
    private String urlImageItemVideo;
    private String titleVideo;
    private String tvTitleChannel;
    private String viewCountVideo;
    private String publishAt;
    private String idVideo;
    private String privacyStatus;


    public ItemVideoInPlayList(String urlImageItemVideo, String titleVideo, String tvTitleChannel, String viewCountVideo, String publishAt, String idVideo, String privacyStatus) {
        this.urlImageItemVideo = urlImageItemVideo;
        this.titleVideo = titleVideo;
        this.tvTitleChannel = tvTitleChannel;
        this.viewCountVideo = viewCountVideo;
        this.publishAt = publishAt;
        this.idVideo = idVideo;
        this.privacyStatus = privacyStatus;
    }

    public String getPrivacyStatus() {
        return privacyStatus;
    }

    public void setPrivacyStatus(String privacyStatus) {
        this.privacyStatus = privacyStatus;
    }

    public String getUrlImageItemVideo() {
        return urlImageItemVideo;
    }

    public void setUrlImageItemVideo(String urlImageItemVideo) {
        this.urlImageItemVideo = urlImageItemVideo;
    }

    public String getTitleVideo() {
        return titleVideo;
    }

    public void setTitleVideo(String titleVideo) {
        this.titleVideo = titleVideo;
    }

    public String getTvTitleChannel() {
        return tvTitleChannel;
    }

    public void setTvTitleChannel(String tvTitleChannel) {
        this.tvTitleChannel = tvTitleChannel;
    }

    public String getViewCountVideo() {
        return viewCountVideo;
    }

    public void setViewCountVideo(String viewCountVideo) {
        this.viewCountVideo = viewCountVideo;
    }

    public String getPublishAt() {
        return publishAt;
    }

    public void setPublishAt(String publishAt) {
        this.publishAt = publishAt;
    }

    public String getIdVideo() {
        return idVideo;
    }

    public void setIdVideo(String idVideo) {
        this.idVideo = idVideo;
    }
}
