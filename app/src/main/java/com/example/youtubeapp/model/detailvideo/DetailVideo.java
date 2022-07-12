package com.example.youtubeapp.model.detailvideo;

import java.util.ArrayList;

public class DetailVideo {
    public String kind;
    public String etag;
    public ArrayList<ItemVideo> items;
    public PageInfoVideo pageInfo;

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

    public ArrayList<ItemVideo> getItems() {
        return items;
    }

    public void setItems(ArrayList<ItemVideo> items) {
        this.items = items;
    }

    public PageInfoVideo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfoVideo pageInfo) {
        this.pageInfo = pageInfo;
    }
}
