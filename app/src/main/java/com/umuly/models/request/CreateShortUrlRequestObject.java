package com.umuly.models.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateShortUrlRequestObject {
    @SerializedName("Title")
    @Expose
    private String title;

    @SerializedName("Description")
    @Expose
    private String description;

    @SerializedName("Tags")
    @Expose
    private String tags;

    @SerializedName("DomainId")
    @Expose
    private String domainId;

    @SerializedName("RedirectUrl")
    @Expose
    private String redirectUrl;

    @SerializedName("Code")
    @Expose
    private String code;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public void setCode(String code) {
        this.code = code;
    }
}


