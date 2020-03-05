package com.umuly.models.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DomainListResponseObject {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("userID")
    @Expose
    private String userId;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("domainUrl")
    @Expose
    private String domainUrl;

    @SerializedName("scheme")
    @Expose
    private String scheme;

    @SerializedName("cfDomainId")
    @Expose
    private String cfDomainId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomainUrl() {
        return domainUrl;
    }

    public void setDomainUrl(String domainUrl) {
        this.domainUrl = domainUrl;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getCfDomainId() {
        return cfDomainId;
    }

    public void setCfDomainId(String cfDomainId) {
        this.cfDomainId = cfDomainId;
    }
}
