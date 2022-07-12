package com.example.youtubeapp.model.detailvideo;


public class ItemVideo {
    public String kind;
    public String etag;
    public String id;
    public SnippetVideo snippet;
    public StatisticsVideo statistics;
    private ContentDetails contentDetails;

    public ContentDetails getContentDetails() {
        return contentDetails;
    }

    public void setContentDetails(ContentDetails contentDetails) {
        this.contentDetails = contentDetails;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SnippetVideo getSnippet() {
        return snippet;
    }

    public void setSnippet(SnippetVideo snippet) {
        this.snippet = snippet;
    }

    public StatisticsVideo getStatistics() {
        return statistics;
    }

    public void setStatistics(StatisticsVideo statistics) {
        this.statistics = statistics;
    }
}
