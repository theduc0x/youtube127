package com.example.youtubeapp.model.listcomment;

public class Comments {
    private String kind;
    private String etag;
    private String id;
    private SnippetCmReplies snippet;

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

    public SnippetCmReplies getSnippet() {
        return snippet;
    }

    public void setSnippet(SnippetCmReplies snippet) {
        this.snippet = snippet;
    }
}
