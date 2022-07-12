package com.example.youtubeapp.model.infochannel;


public class SnippetChannel {
    private String title;
    private String description;
    private String customUrl;
    private String publishedAt;
    private ThumbnailChannel thumbnails;
    private LocalizedChannel localized;
    private String country;

    public String getCustomUrl() {
        return customUrl;
    }

    public void setCustomUrl(String customUrl) {
        this.customUrl = customUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public ThumbnailChannel getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(ThumbnailChannel thumbnails) {
        this.thumbnails = thumbnails;
    }

    public LocalizedChannel getLocalized() {
        return localized;
    }

    public void setLocalized(LocalizedChannel localized) {
        this.localized = localized;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
