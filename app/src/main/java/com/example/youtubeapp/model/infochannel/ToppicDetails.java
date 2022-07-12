package com.example.youtubeapp.model.infochannel;

import java.util.ArrayList;

public class ToppicDetails {
    private ArrayList<String> topicIds;
    private ArrayList<String> topicCategories;

    public ArrayList<String> getTopicIds() {
        return topicIds;
    }

    public void setTopicIds(ArrayList<String> topicIds) {
        this.topicIds = topicIds;
    }

    public ArrayList<String> getTopicCategories() {
        return topicCategories;
    }

    public void setTopicCategories(ArrayList<String> topicCategories) {
        this.topicCategories = topicCategories;
    }
}
