package com.example.youtubeapp.model.itemrecycleview;

import java.io.Serializable;

public class SearchItem implements Serializable {
    private String kindType;
    // channel
    private String idChannel;
    private String titleChannel;
    private String subCount, videoCount;
    private String urlLogoChannel;
    // video
    private String tvTitleVideo, publishAt;
    private String viewCountVideo;
    private String idVideo;
    private String likeCountVideo;
    private String descVideo;
    private String commentCount;
    private String duration;
    // playlist
    private String urlThumbnailsVideo;
    private String videoCountPlayList;
    private String idPlayList;


    public SearchItem(String kindType, String idChannel, String titleChannel, String subCount,
                      String videoCount, String urlLogoChannel, String tvTitleVideo,
                      String publishAt, String viewCountVideo,
                      String idVideo, String likeCountVideo, String descVideo, String commentCount,
                      String urlThumbnailsVideo, String videoCountPlayList,
                      String idPlayList, String duration) {
        this.kindType = kindType;
        this.idChannel = idChannel;
        this.titleChannel = titleChannel;
        this.subCount = subCount;
        this.videoCount = videoCount;
        this.urlLogoChannel = urlLogoChannel;
        this.tvTitleVideo = tvTitleVideo;
        this.publishAt = publishAt;
        this.viewCountVideo = viewCountVideo;
        this.idVideo = idVideo;
        this.likeCountVideo = likeCountVideo;
        this.descVideo = descVideo;
        this.commentCount = commentCount;
        this.urlThumbnailsVideo = urlThumbnailsVideo;
        this.videoCountPlayList = videoCountPlayList;
        this.idPlayList = idPlayList;
        this.duration = duration;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getKindType() {
        return kindType;
    }

    public void setKindType(String kindType) {
        this.kindType = kindType;
    }

    public String getIdChannel() {
        return idChannel;
    }

    public void setIdChannel(String idChannel) {
        this.idChannel = idChannel;
    }

    public String getTitleChannel() {
        return titleChannel;
    }

    public void setTitleChannel(String titleChannel) {
        this.titleChannel = titleChannel;
    }

    public String getSubCount() {
        return subCount;
    }

    public void setSubCount(String subCount) {
        this.subCount = subCount;
    }

    public String getVideoCount() {
        return videoCount;
    }

    public void setVideoCount(String videoCount) {
        this.videoCount = videoCount;
    }

    public String getUrlLogoChannel() {
        return urlLogoChannel;
    }

    public void setUrlLogoChannel(String urlLogoChannel) {
        this.urlLogoChannel = urlLogoChannel;
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


    public String getViewCountVideo() {
        return viewCountVideo;
    }

    public void setViewCountVideo(String viewCountVideo) {
        this.viewCountVideo = viewCountVideo;
    }

    public String getIdVideo() {
        return idVideo;
    }

    public void setIdVideo(String idVideo) {
        this.idVideo = idVideo;
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

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getUrlThumbnailsVideo() {
        return urlThumbnailsVideo;
    }

    public void setUrlThumbnailsVideo(String urlThumbnailsVideo) {
        this.urlThumbnailsVideo = urlThumbnailsVideo;
    }

    public String getVideoCountPlayList() {
        return videoCountPlayList;
    }

    public void setVideoCountPlayList(String videoCountPlayList) {
        this.videoCountPlayList = videoCountPlayList;
    }


    public String getIdPlayList() {
        return idPlayList;
    }

    public void setIdPlayList(String idPlayList) {
        this.idPlayList = idPlayList;
    }
}
