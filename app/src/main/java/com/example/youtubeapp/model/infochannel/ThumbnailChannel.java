package com.example.youtubeapp.model.infochannel;

import com.google.gson.annotations.SerializedName;

public class ThumbnailChannel {
    @SerializedName("default")
    private DefaultsChannel defaults;

    private MediumChannel medium;
    private HighChannel high;

    public DefaultsChannel getDefaults() {
        return defaults;
    }

    public void setDefaults(DefaultsChannel defaults) {
        this.defaults = defaults;
    }

    public MediumChannel getMedium() {
        return medium;
    }

    public void setMedium(MediumChannel medium) {
        this.medium = medium;
    }

    public HighChannel getHigh() {
        return high;
    }

    public void setHigh(HighChannel high) {
        this.high = high;
    }
}
