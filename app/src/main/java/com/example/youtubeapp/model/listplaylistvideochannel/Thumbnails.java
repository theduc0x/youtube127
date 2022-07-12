package com.example.youtubeapp.model.listplaylistvideochannel;

import com.google.gson.annotations.SerializedName;

public class Thumbnails {
    @SerializedName("default")
    public DefaultList mydefault;
    public Medium medium;
    public High high;
    public Standard standard;
    public Maxres maxres;


    public DefaultList getMydefault() {
        return mydefault;
    }

    public void setMydefault(DefaultList mydefault) {
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
