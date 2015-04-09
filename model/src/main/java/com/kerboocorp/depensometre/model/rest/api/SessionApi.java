package com.kerboocorp.depensometre.model.rest.api;

import com.kerboocorp.depensometre.model.entities.AccessToken;
import com.kerboocorp.depensometre.model.entities.Movement;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by cgo on 7/04/2015.
 */
public interface SessionApi {

    @FormUrlEncoded
    @POST("/session")
    void login(@Field("Email") String email, @Field("Password") String password, Callback<AccessToken> callback);

}
