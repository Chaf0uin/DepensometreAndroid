package com.kerboocorp.depensometre.domain.session;

import com.kerboocorp.depensometre.domain.UseCase;
import com.kerboocorp.depensometre.model.entities.AccessToken;
import com.kerboocorp.depensometre.model.entities.ResponseError;

/**
 * Created by chris on 8/04/15.
 */
public interface Login extends UseCase {

    public void onAccessTokenReceived(AccessToken response);

    public void onErrorReceived(ResponseError error);

    public void requestAccessToken();

    public void sendAccessTokenToPresenter(AccessToken response);

    public void sendErrorToPresenter(ResponseError error);

    public void unRegister();

    public void register();

    public void setEmailAndPassword(String email, String password);
}
