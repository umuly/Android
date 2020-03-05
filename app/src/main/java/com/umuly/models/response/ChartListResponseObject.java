package com.umuly.models.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChartListResponseObject {
    @SerializedName("count")
    @Expose
    private Integer count;

    @SerializedName("_id")
    @Expose
    private ChartListIdObject dateObject;


    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public ChartListIdObject getDateObject() {
        return dateObject;
    }

    public void setDateObject(ChartListIdObject dateObject) {
        this.dateObject = dateObject;
    }
}
