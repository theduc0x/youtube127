package com.example.youtubeapp.model.listreplies;

import java.util.ArrayList;

public class Replies {
    private String kind;
    private String etag;
    private PageInfoR pageInfo;
    ArrayList<ItemsR> items;
    private String nextPageToken;

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

    public PageInfoR getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfoR pageInfo) {
        this.pageInfo = pageInfo;
    }

    public ArrayList<ItemsR> getItems() {
        return items;
    }

    public void setItems(ArrayList<ItemsR> items) {
        this.items = items;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }
}
