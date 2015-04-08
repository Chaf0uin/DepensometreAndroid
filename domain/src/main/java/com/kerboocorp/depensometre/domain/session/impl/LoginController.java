package com.kerboocorp.depensometre.domain.session.impl;

import com.kerboocorp.depensometre.common.utils.BusProvider;
import com.kerboocorp.depensometre.domain.session.Login;
import com.kerboocorp.depensometre.model.entities.AccessToken;
import com.kerboocorp.depensometre.model.rest.SessionRestSource;
import com.squareup.otto.Bus;

/**
 * Created by chris on 8/04/15.
 */
public class LoginController implements Login {

    private final SessionRestSource sessionRestSource;
    private final Bus uiBus;

    public LoginController(SessionRestSource sessionRestSource, Bus uiBus) {
        if (sessionRestSource == null)
            throw new IllegalArgumentException("MovementRestSource cannot be null");

        if (uiBus == null)
            throw new IllegalArgumentException("Bus cannot be null");

        this.sessionRestSource = sessionRestSource;
        this.uiBus = uiBus;

        BusProvider.getRestBusInstance().register(this);
    }

    @Override
    public void execute() {
        requestAccessToken();
    }

    @Override
    public void onAccessTokenReceived(AccessToken response) {
        sendAccessTokenToPresenter(response);
    }

    @Override
    public void requestAccessToken() {
        sessionRestSource.login("darkyunsung@gmail.com", "0Mgdessous");
    }

    @Override
    public void sendAccessTokenToPresenter(AccessToken response) {
        uiBus.post(response);
    }

    @Override
    public void unRegister() {
        BusProvider.getRestBusInstance().unregister(this);
    }

}
