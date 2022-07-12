package com.example.youtubeapp.model.listvideohome;

import com.google.gson.annotations.SerializedName;

public class Thumbnail {

    @SerializedName("default")
    private Defaultss defaults;

    private Medium medium;
    private High high;
    private Standard standard;
    private Maxres maxres;

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

    public Defaultss getDefaults() {
        return defaults;
    }

    public void setDefaults(Defaultss defaults) {
        this.defaults = defaults;
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
}

