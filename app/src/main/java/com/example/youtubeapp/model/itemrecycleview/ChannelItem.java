package com.example.youtubeapp.model.itemrecycleview;

public class ChannelItem {
    private String idChannel;
    private String titleChannel;
    private String subCount, videoCount;
    private String urlLogoChannel;

    public ChannelItem(String idChannel, String titleChannel, String subCount, String videoCount, String urlLogoChannel) {
        this.idChannel = idChannel;
        this.titleChannel = titleChannel;
        this.subCount = subCount;
        this.videoCount = videoCount;
        this.urlLogoChannel = urlLogoChannel;
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
}
