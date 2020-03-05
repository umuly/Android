package com.umuly.models.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateShortUrlListResponseObject {
    @SerializedName("shortUrl")
    @Expose
    private String shortUrl;

    public String getShortUrl() {
        return shortUrl;
    }
}
