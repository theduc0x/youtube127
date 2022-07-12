package com.example.youtubeapp.model.listcomment;
import java.util.ArrayList;

public class Comment {
    private String kind;
    private String etag;
    private String nextPageToken;
    private PageInfoComment pageInfo;
    ArrayList<ItemsComment> items;

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

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public PageInfoComment getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfoComment pageInfo) {
        this.pageInfo = pageInfo;
    }

    public ArrayList<ItemsComment> getItems() {
        return items;
    }

    public void setItems(ArrayList<ItemsComment> items) {
        this.items = items;
    }
}
