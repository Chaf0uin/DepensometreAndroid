package com.kerboocorp.depensometre.model.rest;

import com.kerboocorp.depensometre.common.utils.BusProvider;
import com.kerboocorp.depensometre.common.utils.Constants;
import com.kerboocorp.depensometre.model.SessionDataSource;
import com.kerboocorp.depensometre.model.entities.AccessToken;
import com.kerboocorp.depensometre.model.entities.ResponseError;
import com.kerboocorp.depensometre.model.entities.ResponseType;
import com.kerboocorp.depensometre.model.rest.api.MovementApi;
import com.kerboocorp.depensometre.model.rest.api.SessionApi;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by cgo on 8/04/2015.
 */
public class SessionRestSource implements SessionDataSource {

    public static SessionRestSource INSTANCE;
    private final SessionApi sessionApi;

    private SessionRestSource() {

        RestAdapter sessionAPIRest = new RestAdapter.Builder()
                .setEndpoint(Constants.SERVICE_URL)
                .setLogLevel(RestAdapter.LogLevel.HEADERS_AND_ARGS)
                .build();

        sessionApi = sessionAPIRest.create(SessionApi.class);
    }

    public static SessionRestSource getInstance() {

        if (INSTANCE == null)
            INSTANCE = new SessionRestSource();

        return INSTANCE;
    }

    @Override
    public void login(String email, String password) {
        //sessionApi.login(email, password, new Callback<AccessToken>() {
        sessionApi.login(new SessionRequest(email, password), new Callback<AccessToken>() {
            @Override
            public void success(AccessToken accessToken, Response response) {
                BusProvider.getRestBusInstance().post(accessToken);
            }

            @Override
            public void failure(RetrofitError error) {
                System.out.printf("[DEBUG] SessionRestSource failure - " + error.getMessage());
                BusProvider.getRestBusInstance().post(new ResponseError(error, ResponseType.find));
            }
        });
    }
}
