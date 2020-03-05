package com.umuly.models.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class CreateShortUrlResponseObject {
    @SerializedName("status")
    @Expose
    private Integer status;

    @SerializedName("statusText")
    @Expose
    private String statusText;

    @SerializedName("item")
    @Expose
    private CreateShortUrlListResponseObject shortUrl;

    public Integer getStatus() {
        return status;
    }

    public String getStatusText() {
        return statusText;
    }

    public CreateShortUrlListResponseObject getShortUrl() {
        return shortUrl;
    }
}
