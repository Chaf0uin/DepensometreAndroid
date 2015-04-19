package com.kerboocorp.depensometre.mvp.presenters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.kerboocorp.depensometre.R;
import com.kerboocorp.depensometre.common.utils.BusProvider;
import com.kerboocorp.depensometre.domain.session.impl.LoginController;
import com.kerboocorp.depensometre.model.entities.AccessToken;
import com.kerboocorp.depensometre.model.entities.ResponseError;
import com.kerboocorp.depensometre.model.entities.ResponseType;
import com.kerboocorp.depensometre.model.rest.SessionRestSource;
import com.kerboocorp.depensometre.mvp.views.LoginView;
import com.squareup.otto.Subscribe;

/**
 * Created by chris on 8/04/15.
 */
public class LoginPresenter extends Presenter {

    private final LoginView loginView;
    private LoginController loginController;

    private boolean isLoading = false;
    private boolean isRegistered;

    private String email;

    public LoginPresenter(LoginView loginView) {
        this.loginView = loginView;
        loginController = new LoginController(SessionRestSource.getInstance(), BusProvider.getUIBusInstance());
    }

    public void login(String email, String password) {
        loginView.showLoading();
        loginController.setEmailAndPassword(email, password);
        this.email = email;
        loginController.execute();
    }

    @Subscribe
    public void onAccessTokenReceived(AccessToken response) {
        loginView.hideLoading();
        SharedPreferences sharedPref = loginView.getContext().getSharedPreferences(loginView.getContext().getString(R.string.app_full_name), Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(loginView.getContext().getString(R.string.access_token), response.getToken());
        editor.putString(loginView.getContext().getString(R.string.email), email);
        editor.commit();

        loginView.startMovementListActivity();
    }

    @Subscribe
    public void onErrorReceived(ResponseError error) {
        loginView.hideLoading();
        if (ResponseType.find.equals(error.getType())) {
            loginView.showError(loginView.getContext().getString(R.string.error_login));
        }
    }

    @Override
    public void start() {
        if (!isRegistered) {
            BusProvider.getUIBusInstance().register(this);
            isRegistered = true;

            loginController.register();
        }

        SharedPreferences sharedPref = loginView.getContext().getSharedPreferences(loginView.getContext().getString(R.string.app_full_name), Context.MODE_PRIVATE);
        String accessToken = sharedPref.getString(loginView.getContext().getString(R.string.access_token), "");

        if (!accessToken.equals("")) {
            loginView.startMovementListActivity();
        }


    }

    @Override
    public void stop() {
        if (isRegistered) {
            BusProvider.getUIBusInstance().unregister(this);
            isRegistered = false;
        }

        loginController.unRegister();
    }
}
