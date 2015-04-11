package com.kerboocorp.depensometre.model.rest.api;

import com.kerboocorp.depensometre.model.entities.Movement;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;

/**
 * Created by cgo on 7/04/2015.
 */
public interface MovementApi {

    @GET("/movements")
    void findMovementList(@Header("Access-Token") String accessToken, @Header("Year") String year, @Header("Month") String month, Callback<List<Movement>> callback);

    @POST("/movements")
    void insertMovement(@Header("Access-Token") String accessToken, @Body Movement movement, Callback<Movement> callback);

}
