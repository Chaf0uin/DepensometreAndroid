package com.kerboocorp.depensometre.views.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.kerboocorp.depensometre.R;
import com.kerboocorp.depensometre.model.entities.MovementList;
import com.kerboocorp.depensometre.mvp.presenters.LoginPresenter;
import com.kerboocorp.depensometre.mvp.presenters.MovementListPresenter;
import com.kerboocorp.depensometre.mvp.views.LoginView;
import com.kerboocorp.depensometre.views.adapters.MovementAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by cgo on 8/04/2015.
 */
public class LoginActivity extends ActionBarActivity implements LoginView {

    private LoginPresenter loginPresenter;

    @InjectView(R.id.emailEditText)
    EditText emailEditText;
    @InjectView(R.id.passwordEditText)
    EditText passwordEditText;
    @InjectView(R.id.loginButton)
    Button loginButton;

    private MaterialDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        loginPresenter = new LoginPresenter(this);

    }

    @Override
    protected void onStop() {

        super.onStop();
        loginPresenter.stop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loginPresenter.start();
    }

    @Override
    public void showLoading() {
        progressDialog = new MaterialDialog.Builder(this)
                .title(getString(R.string.dialog_login))
                .content(getString(R.string.dialog_wait))
                .progress(true, 0)
                .show();

        progressDialog.show();
    }

    @Override
    public void hideLoading() {
        progressDialog.dismiss();
    }

    @Override
    public void showError(String error) {
        new MaterialDialog.Builder(this)
                .title(getString(R.string.error_oops))
                .content(error)
                .positiveText(R.string.dialog_delete_agree)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        dialog.dismiss();
                    }

                })
                .show();
    }

    @Override
    public void hideError() {

    }

    @Override
    public void login() {
        loginPresenter.login(emailEditText.getText().toString(), passwordEditText.getText().toString());
    }

    @Override
    public void startMovementListActivity() {
        Intent intent = new Intent(this, MovementListActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public Context getContext() {
        return this;
    }
}
