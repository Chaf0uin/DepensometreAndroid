package com.kerboocorp.depensometre.model.rest;

import com.kerboocorp.depensometre.common.utils.Constants;
import com.kerboocorp.depensometre.model.SessionDataSource;
import com.kerboocorp.depensometre.model.rest.api.MovementApi;
import com.kerboocorp.depensometre.model.rest.api.SessionApi;

import retrofit.RestAdapter;

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

    }
}
