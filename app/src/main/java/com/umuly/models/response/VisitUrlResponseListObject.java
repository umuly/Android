package com.umuly.models.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VisitUrlResponseListObject {
    @SerializedName("languageCode")
    @Expose
    private String languageCode;

    @SerializedName("updatedOn")
    @Expose
    private String date;

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
