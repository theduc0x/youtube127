package com.example.youtubeapp.model.listcomment;

public class TopLevelComment {
    private String kind;
    private String etag;
    private String id;
    private SnippetTopComment snippet;

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

    public SnippetTopComment getSnippet() {
        return snippet;
    }

    public void setSnippet(SnippetTopComment snippet) {
        this.snippet = snippet;
    }
}
