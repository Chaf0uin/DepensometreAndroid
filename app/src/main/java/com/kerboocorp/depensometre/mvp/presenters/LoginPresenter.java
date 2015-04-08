package com.kerboocorp.depensometre.mvp.presenters;

import com.kerboocorp.depensometre.mvp.views.LoginView;

/**
 * Created by chris on 8/04/15.
 */
public class LoginPresenter extends Presenter {

    private final LoginView loginView;

    public LoginPresenter(LoginView loginView) {
        this.loginView = loginView;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}
