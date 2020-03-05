package com.umuly.models.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllUrlListResponseObject {
    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("tags")
    @Expose
    private String tags;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("userID")
    @Expose
    private String userID;

    @SerializedName("domainID")
    @Expose
    private String domainID;

    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("redirectUrl")
    @Expose
    private String redirectUrl;

    @SerializedName("scheme")
    @Expose
    private String scheme;

    @SerializedName("shortUrl")
    @Expose
    private String shortUrl;

    @SerializedName("visitCount")
    @Expose
    private Integer visitCount;

    @SerializedName("status")
    @Expose
    private Integer status;

    @SerializedName("statusText")
    @Expose
    private String statusText;

    @SerializedName("updatedOn")
    @Expose
    private String updatedOn;

    @SerializedName("createdOn")
    @Expose
    private String createdOn;

    @SerializedName("createdBy")
    @Expose
    private String createdBy;

    @SerializedName("updatedBy")
    @Expose
    private String updatedBy;

    public String getTitle() {
        if (title == null)
            return "";
        return title;
    }

    public String getDescription() {
        if (description == null)
            return "";
        return description;
    }

    public String getTags() {
        if (tags == null)
            return "";
        return tags;
    }

    public String getId() {
        return id;
    }

    public String getUserID() {
        return userID;
    }

    public String getDomainID() {
        return domainID;
    }

    public String getCode() {
        return code;
    }

    public String getRedirectUrl() {
        if (redirectUrl == null)
            return "";
        return redirectUrl;
    }

    public String getScheme() {
        return scheme;
    }

    public String getShortUrl() {
        if (shortUrl == null)
            return "";
        return shortUrl;
    }

    public Integer getVisitCount() {
        return visitCount;
    }

    public Integer getStatus() {
        return status;
    }

    public String getStatusText() {
        return statusText;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }
}
