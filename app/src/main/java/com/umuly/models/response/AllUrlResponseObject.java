package com.umuly.models.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllUrlResponseObject {
    @SerializedName("status")
    @Expose
    private Integer status;

    @SerializedName("statusText")
    @Expose
    private String statusText;

    @SerializedName("item")
    @Expose
    private List<AllUrlListResponseObject> urlList;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public List<AllUrlListResponseObject> getUrlList() {
        return urlList;
    }

    public void setUrlList(List<AllUrlListResponseObject> urlList) {
        this.urlList = urlList;
    }
}
