package com.umuly.models.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChartRequestObject {
    @SerializedName("Url")
    @Expose
    private String url;

    @SerializedName("StartDateTime")
    @Expose
    private String startDate;

    @SerializedName("EndDateTime")
    @Expose
    private String endDate;

    @SerializedName("VisitType")
    @Expose
    private Integer visitTypep;


    public void setUrl(String url) {
        this.url = url;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setVisitTypep(Integer visitTypep) {
        this.visitTypep = visitTypep;
    }
}
