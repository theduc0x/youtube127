package com.example.youtubeapp.model.infochannel;

public class Itemss {
    private String kind;
    private String etag;
    private String id;
    private SnippetChannel snippet;
    private ContentDetailsChannel contentDetails;
    private StatisticsChannel statistics;
    private ToppicDetails topicDetails;
    private BrandingSettings brandingSettings;

    public BrandingSettings getBrandingSettings() {
        return brandingSettings;
    }

    public void setBrandingSettings(BrandingSettings brandingSettings) {
        this.brandingSettings = brandingSettings;
    }

    public ToppicDetails getTopicDetails() {
        return topicDetails;
    }


    public void setTopicDetails(ToppicDetails topicDetails) {
        this.topicDetails = topicDetails;
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

    public SnippetChannel getSnippet() {
        return snippet;
    }

    public void setSnippet(SnippetChannel snippet) {
        this.snippet = snippet;
    }

    public ContentDetailsChannel getContentDetails() {
        return contentDetails;
    }

    public void setContentDetails(ContentDetailsChannel contentDetails) {
        this.contentDetails = contentDetails;
    }

    public StatisticsChannel getStatistics() {
        return statistics;
    }

    public void setStatistics(StatisticsChannel statistics) {
        this.statistics = statistics;
    }
}
