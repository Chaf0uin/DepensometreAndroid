package com.kerboocorp.depensometre.mvp.views;

/**
 * Created by chris on 8/04/15.
 */
public interface LoginView {

    void showLoading ();

    void hideLoading ();

    void showError (String error);

    void hideError ();

    void login();
}
