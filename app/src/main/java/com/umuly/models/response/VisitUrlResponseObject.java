package com.umuly.models.response;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VisitUrlResponseObject {
    @SerializedName("status")
    @Expose
    private Integer status;

    @SerializedName("statusText")
    @Expose
    private String statusText;

    @SerializedName("item")
    @Expose
    private List<VisitUrlResponseListObject> urlVisitList;

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

    public List<VisitUrlResponseListObject> getUrlVisitList() {
        return urlVisitList;
    }

    public void setUrlVisitList(List<VisitUrlResponseListObject> urlVisitList) {
        this.urlVisitList = urlVisitList;
    }
}
