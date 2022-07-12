package com.example.youtubeapp.model.infochannel;

import java.util.ArrayList;

public class Channel {
    private String kind;
    private String etag;
    private PageInfoss pageInfo;
    ArrayList<Itemss> items;

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

    public PageInfoss getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfoss pageInfo) {
        this.pageInfo = pageInfo;
    }

    public ArrayList<Itemss> getItems() {
        return items;
    }

    public void setItems(ArrayList<Itemss> items) {
        this.items = items;
    }
}
