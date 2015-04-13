package com.kerboocorp.depensometre.model.rest.api;

import com.kerboocorp.depensometre.model.entities.Movement;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

/**
 * Created by cgo on 7/04/2015.
 */
public interface MovementApi {

    @GET("/movements")
    void findMovementList(@Header("Access-Token") String accessToken, @Header("Year") String year, @Header("Month") String month, Callback<List<Movement>> callback);

    @POST("/movements")
    void insertMovement(@Header("Access-Token") String accessToken, @Body Movement movement, Callback<Movement> callback);

    @DELETE("/movements/{id}")
    void deleteMovement(@Header("Access-Token") String accessToken, @Path("id") Long id, Callback<Movement> callback);

    @PUT("/movements/{id}")
    void updateMovement(@Header("Access-Token") String accessToken, @Path("id") Long id, @Body Movement movement, Callback<Boolean> callback);

}
