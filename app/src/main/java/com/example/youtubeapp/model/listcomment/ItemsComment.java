package com.example.youtubeapp.model.listcomment;

public class ItemsComment {
    private String kind;
    private String etag;
    private String id;
    private SnippetComment snippet;
    private RepliesComment replies;

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

    public SnippetComment getSnippet() {
        return snippet;
    }

    public void setSnippet(SnippetComment snippet) {
        this.snippet = snippet;
    }

    public RepliesComment getReplies() {
        return replies;
    }

    public void setReplies(RepliesComment replies) {
        this.replies = replies;
    }
}
