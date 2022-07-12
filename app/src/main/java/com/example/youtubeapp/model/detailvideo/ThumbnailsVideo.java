package com.example.youtubeapp.model.detailvideo;

import com.google.gson.annotations.SerializedName;

public class ThumbnailsVideo {
    @SerializedName("default")
    public DefaultVideo mydefault;
    public MediumV medium;
    public High high;

    public DefaultVideo getMydefault() {
        return mydefault;
    }

    public void setMydefault(DefaultVideo mydefault) {
        this.mydefault = mydefault;
    }

    public MediumV getMedium() {
        return medium;
    }

    public void setMedium(MediumV medium) {
        this.medium = medium;
    }

    public High getHigh() {
        return high;
    }

    public void setHigh(High high) {
        this.high = high;
    }
}
