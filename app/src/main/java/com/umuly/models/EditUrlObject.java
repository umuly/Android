package com.umuly.models;


import java.io.Serializable;

public class EditUrlObject implements Serializable {
    private String title;
    private String tags;
    private String desc;
    private String shortUrl;
    private String domain;
    private String domainId;
    private String scheme;
    private String redirectUrl;

    public String getRedirectUrl() {
        if (redirectUrl == null)
            return "";
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getScheme() {
        if (scheme == null)
            return "";
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getTitle() {
        if (title == null)
            return "";
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTags() {
        if (tags == null)
            return "";
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getDesc() {
        if (desc == null)
            return "";
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getDomain() {
        if (domain == null)
            return "";
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }


}
