package com.example.youtubeapp.model.searchyoutube;


import java.util.ArrayList;

public class Search {
    public String kind;
    public String etag;
    public String nextPageToken;
    public String regionCode;
    public PageInfoRelated pageInfo;
    public ArrayList<ItemsSearch> items;

    public PageInfoRelated getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfoRelated pageInfo) {
        this.pageInfo = pageInfo;
    }

    public ArrayList<ItemsSearch> getItems() {
        return items;
    }


    public void setItems(ArrayList<ItemsSearch> items) {
        this.items = items;
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

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }



}
