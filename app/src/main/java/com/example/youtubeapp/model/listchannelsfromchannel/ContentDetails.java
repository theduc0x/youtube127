package com.example.youtubeapp.model.listchannelsfromchannel;

import java.util.ArrayList;

public class ContentDetails {
    public ArrayList<String> playlists;
    public ArrayList<String> channels;

    public ArrayList<String> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(ArrayList<String> playlists) {
        this.playlists = playlists;
    }

    public ArrayList<String> getChannels() {
        return channels;
    }

    public void setChannels(ArrayList<String> channels) {
        this.channels = channels;
    }
}
