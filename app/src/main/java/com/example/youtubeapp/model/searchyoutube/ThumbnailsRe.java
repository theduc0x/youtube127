package com.example.youtubeapp.model.searchyoutube;

import com.google.gson.annotations.SerializedName;

public class ThumbnailsRe {
    @SerializedName("default")
    public DefaultRe mydefault;
    public MediumRe medium;
    public HighRe high;
    public StandardRe standard;
    public MaxresRe maxres;

    public DefaultRe getMydefault() {
        return mydefault;
    }

    public void setMydefault(DefaultRe mydefault) {
        this.mydefault = mydefault;
    }

    public MediumRe getMedium() {
        return medium;
    }

    public void setMedium(MediumRe medium) {
        this.medium = medium;
    }

    public HighRe getHigh() {
        return high;
    }

    public void setHigh(HighRe high) {
        this.high = high;
    }

    public StandardRe getStandard() {
        return standard;
    }

    public void setStandard(StandardRe standard) {
        this.standard = standard;
    }

    public MaxresRe getMaxres() {
        return maxres;
    }

    public void setMaxres(MaxresRe maxres) {
        this.maxres = maxres;
    }
}
