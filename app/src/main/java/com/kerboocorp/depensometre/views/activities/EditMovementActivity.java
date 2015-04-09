package com.kerboocorp.depensometre.views.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.kerboocorp.depensometre.R;
import com.kerboocorp.depensometre.mvp.presenters.EditMovementPresenter;
import com.kerboocorp.depensometre.mvp.presenters.LoginPresenter;
import com.kerboocorp.depensometre.mvp.views.EditMovementView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by cgo on 9/04/2015.
 */
public class EditMovementActivity extends ActionBarActivity implements EditMovementView {

    private EditMovementPresenter editMovementPresenter;

    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_movement);
        ButterKnife.inject(this);

        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            editMovementPresenter = new EditMovementPresenter(this);
        } else {

        }
    }

    @Override
    public void cancel() {

    }

    @Override
    public void save() {

    }

    @Override
    public Context getContext() {
        return this;
    }
}
