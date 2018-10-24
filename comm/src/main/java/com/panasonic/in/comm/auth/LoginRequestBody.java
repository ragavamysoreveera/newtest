package com.panasonic.in.comm.auth;

import com.google.gson.annotations.SerializedName;

public class LoginRequestBody {
    @SerializedName("user")
    public UserModel mUserModel;

    public LoginRequestBody(String username, String password) {
        mUserModel = new UserModel();
        mUserModel.mLoginId = username;
        mUserModel.mPassword = password;
    }

    public static class UserModel {
        @SerializedName("login")
        public String mLoginId;

        @SerializedName("password")
        public String mPassword;
    }
}
