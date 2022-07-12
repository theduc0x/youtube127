package com.example.youtubeapp.model.listreplies;

public class ItemsR {
    private String kind;
    private String etag;
    private String id;
    private SnippetR snippet;

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

    public SnippetR getSnippet() {
        return snippet;
    }

    public void setSnippet(SnippetR snippet) {
        this.snippet = snippet;
    }
}
