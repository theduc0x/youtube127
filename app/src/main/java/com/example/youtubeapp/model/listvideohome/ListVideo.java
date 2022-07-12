package com.example.youtubeapp.model.listvideohome;

import java.util.ArrayList;

public class ListVideo {
        private String kind;
        private String etag;
        private ArrayList<Items> items;
        private String nextPageToken;
        private PageInfos pageInfo;

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

        public ArrayList<Items> getItems() {
                return items;
        }

        public void setItems(ArrayList<Items> items) {
                this.items = items;
        }

        public String getNextPageToken() {
                return nextPageToken;
        }

        public void setNextPageToken(String nextPageToken) {
                this.nextPageToken = nextPageToken;
        }

        public PageInfos getPageInfo() {
                return pageInfo;
        }

        public void setPageInfo(PageInfos pageInfo) {
                this.pageInfo = pageInfo;
        }
}


