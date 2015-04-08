package com.kerboocorp.depensometre.domain.session;

import com.kerboocorp.depensometre.domain.UseCase;
import com.kerboocorp.depensometre.model.entities.AccessToken;

/**
 * Created by chris on 8/04/15.
 */
public interface Login extends UseCase {

    public void onAccessTokenReceived(AccessToken response);

    public void requestAccessToken();

    public void sendAccessTokenToPresenter(AccessToken response);

    public void unRegister();
}
