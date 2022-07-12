package com.example.youtubeapp.model.playlistitem;

import com.google.gson.annotations.SerializedName;

public class Thumbnails {
    @SerializedName("default")

    public DefaultItem mydefault;
    public Medium medium;
    public High high;
    public Standard standard;
    public Maxres maxres;

    public DefaultItem getMydefault() {
        return mydefault;
    }

    public void setMydefault(DefaultItem mydefault) {
        this.mydefault = mydefault;
    }

    public Medium getMedium() {
        return medium;
    }

    public void setMedium(Medium medium) {
        this.medium = medium;
    }

    public High getHigh() {
        return high;
    }

    public void setHigh(High high) {
        this.high = high;
    }

    public Standard getStandard() {
        return standard;
    }

    public void setStandard(Standard standard) {
        this.standard = standard;
    }

    public Maxres getMaxres() {
        return maxres;
    }

    public void setMaxres(Maxres maxres) {
        this.maxres = maxres;
    }
}
