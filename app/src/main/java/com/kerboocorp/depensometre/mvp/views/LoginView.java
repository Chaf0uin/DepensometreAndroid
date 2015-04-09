package com.kerboocorp.depensometre.mvp.views;

/**
 * Created by chris on 8/04/15.
 */
public interface LoginView extends View {

    void showLoading ();

    void hideLoading ();

    void showError (String error);

    void hideError ();

    void login();

    void startMovementListActivity();
}
