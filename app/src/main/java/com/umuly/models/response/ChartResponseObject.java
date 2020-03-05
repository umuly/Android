package com.umuly.models.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChartResponseObject {

    @SerializedName("status")
    @Expose
    private Integer status;

    @SerializedName("statusText")
    @Expose
    private String statusText;

    @SerializedName("item")
    @Expose
    private List<ChartListResponseObject> chartList;

    @SerializedName("itemCount")
    @Expose
    private Integer itemCount;

    @SerializedName("skipCount")
    @Expose
    private Integer skipCount;

    @SerializedName("requestDate")
    @Expose
    private String requestDate;

    @SerializedName("responseDate")
    @Expose
    private String responseDate;

    public Integer getStatus() {
        return status;
    }

    public String getStatusText() {
        return statusText;
    }

    public List<ChartListResponseObject> getChartList() {
        return chartList;
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public Integer getSkipCount() {
        return skipCount;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public String getResponseDate() {
        return responseDate;
    }


}
