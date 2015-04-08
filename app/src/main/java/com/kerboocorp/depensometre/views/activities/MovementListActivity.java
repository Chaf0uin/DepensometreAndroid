package com.kerboocorp.depensometre.views.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;

import com.kerboocorp.depensometre.R;
import com.kerboocorp.depensometre.model.entities.Movement;
import com.kerboocorp.depensometre.model.entities.MovementList;
import com.kerboocorp.depensometre.mvp.presenters.MovementListPresenter;
import com.kerboocorp.depensometre.mvp.views.MovementListView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by cgo on 8/04/2015.
 */
public class MovementListActivity extends ActionBarActivity implements MovementListView{

    private MovementListPresenter movementListPresenter;

    @InjectView(R.id.coucouTextView)
    TextView coucouTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movement_list);
        ButterKnife.inject(this);

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
        Log.d("TEST", "Yo");
        //coucouTextView.setText(movementList.getMovementList().size());
        coucouTextView.setText("YEAH");
        List<Movement> mvList = movementList.getMovementList();
        coucouTextView.setText(String.valueOf(mvList.size()));
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(String error) {

    }

    @Override
    public void hideError() {

    }

    @Override
    public boolean isMovementListEmpty() {
        //return (mMoviesAdapter == null) || mMoviesAdapter.getMovieList().isEmpty();
        return true;
    }

    @Override
    public void appendMovementList(MovementList movementList) {

    }

    @Override
    public Context getContext() {
        return null;
    }
}
