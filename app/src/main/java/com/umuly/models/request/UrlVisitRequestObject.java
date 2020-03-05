package com.umuly.models.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UrlVisitRequestObject {
    @SerializedName("UrlId")
    @Expose
    private String urlId;

    public String getUrlId() {
        return urlId;
    }

    public void setUrlId(String urlId) {
        this.urlId = urlId;
    }
}
