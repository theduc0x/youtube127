package com.example.youtubeapp.model.itemrecycleview;

import java.io.Serializable;

public class VideoItem implements Serializable {
    private String urlImageItemVideo;
    private String urlLogoChannel;
    private String tvTitleVideo, publishAt;
    private String tvTitleChannel;
    private String viewCountVideo;
    private String idVideo;
    private String likeCountVideo;
    private String descVideo;
    private String idChannel;
    private String commentCount;
    private String duration;

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getUrlLogoChannel() {
        return urlLogoChannel;
    }

    public void setUrlLogoChannel(String urlLogoChannel) {
        this.urlLogoChannel = urlLogoChannel;
    }

    public String getIdChannel() {
        return idChannel;
    }

    public void setIdChannel(String idChannel) {
        this.idChannel = idChannel;
    }

    public String getLikeCountVideo() {
        return likeCountVideo;
    }

    public void setLikeCountVideo(String likeCountVideo) {
        this.likeCountVideo = likeCountVideo;
    }

    public String getDescVideo() {
        return descVideo;
    }

    public void setDescVideo(String descVideo) {
        this.descVideo = descVideo;
    }

    public VideoItem(String urlImageItemVideo, String urlLogoChannel, String tvTitleVideo,
                     String tvTimeVideo, String tvTitleChannel, String viewCountVideo,
                     String idVideo, String likeCountVideo, String descVideo,
                     String idChannel, String commentCount, String duration) {
        this.urlImageItemVideo = urlImageItemVideo;
        this.urlLogoChannel = urlLogoChannel;
        this.tvTitleVideo = tvTitleVideo;
        this.publishAt = tvTimeVideo;
        this.tvTitleChannel = tvTitleChannel;
        this.viewCountVideo = viewCountVideo;
        this.idVideo = idVideo;
        this.likeCountVideo = likeCountVideo;
        this.descVideo = descVideo;
        this.idChannel = idChannel;
        this.commentCount = commentCount;
        this.duration = duration;
    }
    public VideoItem() {

    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getIdVideo() {
        return idVideo;
    }

    public void setIdVideo(String idVideo) {
        this.idVideo = idVideo;
    }

    public String getViewCountVideo() {
        return viewCountVideo;
    }

    public void setViewCountVideo(String viewCountVideo) {
        this.viewCountVideo = viewCountVideo;
    }

    public String getUrlImageItemVideo() {
        return urlImageItemVideo;
    }

    public void setUrlImageItemVideo(String urlImageItemVideo) {
        this.urlImageItemVideo = urlImageItemVideo;
    }


    public String getTvTitleVideo() {
        return tvTitleVideo;
    }

    public void setTvTitleVideo(String tvTitleVideo) {
        this.tvTitleVideo = tvTitleVideo;
    }

    public String getPublishAt() {
        return publishAt;
    }

    public void setPublishAt(String publishAt) {
        this.publishAt = publishAt;
    }

    public String getTvTitleChannel() {
        return tvTitleChannel;
    }

    public void setTvTitleChannel(String tvTitleChannel) {
        this.tvTitleChannel = tvTitleChannel;
    }
}
