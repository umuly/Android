package com.umuly.models.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponseObject {
    @SerializedName("token")
    @Expose
    private String token;

    public String getToken() {
        return token;
    }
}
