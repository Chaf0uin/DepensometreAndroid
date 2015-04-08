package com.kerboocorp.depensometre.views.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kerboocorp.depensometre.R;
import com.kerboocorp.depensometre.model.entities.Movement;
import com.kerboocorp.depensometre.model.entities.MovementList;
import com.kerboocorp.depensometre.mvp.presenters.MovementListPresenter;
import com.kerboocorp.depensometre.mvp.views.MovementListView;
import com.kerboocorp.depensometre.views.adapters.MovementAdapter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by cgo on 8/04/2015.
 */
public class MovementListActivity extends ActionBarActivity implements MovementListView {

    private MovementListPresenter movementListPresenter;

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    //DrawerLayout navigationDrawerLayout;
    //private ActionBarDrawerToggle navigationDrawerToggle;
    @InjectView(R.id.progressBarLayout)
    RelativeLayout progressBarLayout;
    //@InjectView(R.id.swipe_container)
    //SwipeRefreshLayout swipeRefreshLayout;
    @InjectView(R.id.movementList)
    RecyclerView movementRecyclerView;

    private MovementAdapter movementAdapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movement_list);
        ButterKnife.inject(this);

        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        movementAdapter = new MovementAdapter(this);
        linearLayoutManager = new LinearLayoutManager(this);

        movementRecyclerView.setAdapter(movementAdapter);
        movementRecyclerView.setLayoutManager(linearLayoutManager);
        movementRecyclerView.setItemAnimator(new DefaultItemAnimator());

        if (savedInstanceState == null) {

            movementListPresenter = new MovementListPresenter(this);

        } else {

//        MoviesWrapper moviesWrapper = (MoviesWrapper) savedInstanceState
//                .getSerializable("movies_wrapper");
//
//        mMoviesPresenter = new MoviesPresenter(this, moviesWrapper);
//    }

        }
    }

    @Override
     protected void onStop() {

        super.onStop();
        movementListPresenter.stop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        movementListPresenter.start();
    }

    @Override
    public void showMovementList(MovementList movementList) {
        movementAdapter.addMovementList(movementList.getMovementList());
    }

    @Override
    public void showLoading() {
        progressBarLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBarLayout.setVisibility(View.GONE);
    }

    @Override
    public void showError(String error) {

    }

    @Override
    public void hideError() {

    }

    @Override
    public boolean isMovementListEmpty() {
        return (movementAdapter == null) || movementAdapter.getMovementList().isEmpty();
    }

    @Override
    public void appendMovementList(MovementList movementList) {

    }

    @Override
    public Context getContext() {
        return null;
    }
}
