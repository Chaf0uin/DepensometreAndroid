package com.kerboocorp.depensometre.domain.session.impl;

import com.kerboocorp.depensometre.common.utils.BusProvider;
import com.kerboocorp.depensometre.domain.session.Login;
import com.kerboocorp.depensometre.model.SessionDataSource;
import com.kerboocorp.depensometre.model.entities.AccessToken;
import com.kerboocorp.depensometre.model.entities.ResponseError;
import com.kerboocorp.depensometre.model.rest.SessionRestSource;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

/**
 * Created by chris on 8/04/15.
 */
public class LoginController implements Login {

    private final SessionDataSource sessionDataSource;
    private final Bus uiBus;

    private String email;
    private String password;

    public LoginController(SessionDataSource sessionDataSource, Bus uiBus) {
        if (sessionDataSource == null)
            throw new IllegalArgumentException("SessionDataSource cannot be null");

        if (uiBus == null)
            throw new IllegalArgumentException("Bus cannot be null");

        this.sessionDataSource = sessionDataSource;
        this.uiBus = uiBus;

        BusProvider.getRestBusInstance().register(this);
    }

    @Override
    public void execute() {
        requestAccessToken();
    }

    @Subscribe
    @Override
    public void onAccessTokenReceived(AccessToken response) {
        sendAccessTokenToPresenter(response);
    }

    @Subscribe
    @Override
    public void onErrorReceived(ResponseError error) {
        sendErrorToPresenter(error);
    }

    @Override
    public void requestAccessToken() {
        sessionDataSource.login(email, password);
    }

    @Override
    public void sendAccessTokenToPresenter(AccessToken response) {
        uiBus.post(response);
    }

    @Override
    public void sendErrorToPresenter(ResponseError error) {
        uiBus.post(error);
    }

    @Override
    public void unRegister() {
        BusProvider.getRestBusInstance().unregister(this);
    }

    @Override
    public void setEmailAndPassword(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
