package com.umuly.models.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResetPassRequestObject {
    @SerializedName("Email")
    @Expose
    private String email;

    public void setEmail(String email) {
        this.email = email;
    }
}
