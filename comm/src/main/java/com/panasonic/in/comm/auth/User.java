package com.panasonic.in.comm.auth;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("User-Token")
    public String mToken;

    @SerializedName("login")
    public String mLoginId;

    @SerializedName("email")
    public String mEmailId;
}