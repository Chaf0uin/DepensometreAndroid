package com.kerboocorp.depensometre.views.activities;

import android.content.Context;
import android.content.Intent;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.kerboocorp.depensometre.R;
import com.kerboocorp.depensometre.model.entities.Movement;
import com.kerboocorp.depensometre.model.entities.MovementList;
import com.kerboocorp.depensometre.mvp.presenters.MovementListPresenter;
import com.kerboocorp.depensometre.mvp.views.MovementListView;
import com.kerboocorp.depensometre.views.adapters.MovementAdapter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
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
    @InjectView(R.id.progressBarLayout)
    RelativeLayout progressBarLayout;
    @InjectView(R.id.drawer_layout)
    DrawerLayout navigationDrawerLayout;
    @InjectView(R.id.movementList)
    RecyclerView movementRecyclerView;

    @InjectView(R.id.month_spinner)
    Spinner monthSpinner;
    @InjectView(R.id.year_spinner)
    Spinner yearSpinner;
    @InjectView(R.id.select_button)
    Button selectButton;

    @InjectView(R.id.add_input_button)
    FloatingActionButton addInputButton;
    @InjectView(R.id.add_output_button)
    FloatingActionButton addOutputButton;

    private MovementAdapter movementAdapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movement_list);
        ButterKnife.inject(this);

        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);

        movementAdapter = new MovementAdapter(this);
        linearLayoutManager = new LinearLayoutManager(this);

        movementRecyclerView.setAdapter(movementAdapter);
        movementRecyclerView.setLayoutManager(linearLayoutManager);
        movementRecyclerView.setItemAnimator(new DefaultItemAnimator());

        ArrayAdapter<CharSequence> adapterMonth = ArrayAdapter.createFromResource(this,
                R.array.month, android.R.layout.simple_spinner_item);

        adapterMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        monthSpinner.setAdapter(adapterMonth);

        ArrayAdapter<CharSequence> adapterYear = ArrayAdapter.createFromResource(this,
                R.array.year, android.R.layout.simple_spinner_item);

        adapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(adapterYear);

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMonth();
            }
        });

        addOutputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startEditMovementActivity(true);
            }
        });

        addInputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startEditMovementActivity(false);
            }
        });

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
        movementAdapter.clearMovementList();
        movementAdapter.addMovementList(movementList.getMovementList());
    }

    @Override
    public void selectMonth() {
        movementListPresenter.selectMonth(monthSpinner.getSelectedItemPosition(), yearSpinner.getSelectedItemPosition());
    }

    @Override
    public void setTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void closeDrawer() {
        navigationDrawerLayout.closeDrawers();
    }

    @Override
    public void setSelectedMonthSpinner(int month, int year) {
        monthSpinner.setSelection(month);
        yearSpinner.setSelection(year);
    }

    @Override
    public void startEditMovementActivity(boolean isOutput) {
        Intent intent = new Intent(this, EditMovementActivity.class);
        intent.putExtra("isOutput", isOutput);
        startActivity(intent);
    }

    @Override
    public Context getContext() {
        return this;
    }
}
