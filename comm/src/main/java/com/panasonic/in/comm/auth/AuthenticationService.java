package com.panasonic.in.comm.auth;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

//
public interface AuthenticationService {

    @Headers({
            "Content-Type: application/json",
            "Authorization: Token token=0a2bc0fa68941f19300a1c6dd3311f64",
            "Accept: application/vnd.favqs.v2+json"
    })
    @POST("api/session")
    Single<User> login(@Body LoginRequestBody loginRequestBody);



}
