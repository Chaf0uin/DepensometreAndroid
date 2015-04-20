package com.kerboocorp.depensometre.views.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.kerboocorp.depensometre.R;
import com.kerboocorp.depensometre.model.entities.Movement;
import com.kerboocorp.depensometre.model.entities.MovementList;
import com.kerboocorp.depensometre.model.entities.Year;
import com.kerboocorp.depensometre.model.entities.YearList;
import com.kerboocorp.depensometre.mvp.presenters.MovementListPresenter;
import com.kerboocorp.depensometre.mvp.views.MovementListView;
import com.kerboocorp.depensometre.views.adapters.MovementAdapter;

import org.w3c.dom.Text;

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
    @InjectView(R.id.swipe_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @InjectView(R.id.movementList)
    RecyclerView movementRecyclerView;

    @InjectView(R.id.emailTextView)
    TextView emailTextView;
    @InjectView(R.id.month_spinner)
    Spinner monthSpinner;
    @InjectView(R.id.year_spinner)
    Spinner yearSpinner;
    @InjectView(R.id.select_button)
    Button selectButton;
    @InjectView(R.id.logoutTextView)
    TextView logoutTextView;

    @InjectView(R.id.add_input_button)
    FloatingActionButton addInputButton;
    @InjectView(R.id.add_output_button)
    FloatingActionButton addOutputButton;

    private MovementAdapter movementAdapter;
    private LinearLayoutManager linearLayoutManager;

    private MaterialDialog progressDialog;

    private YearList yearList;

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
                startEditMovementActivity(true, null);
            }
        });

        addInputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startEditMovementActivity(false, null);
            }
        });

        if (savedInstanceState == null) {

            movementListPresenter = new MovementListPresenter(this);

        } else {

            MovementList movementList = (MovementList) savedInstanceState.getSerializable("movements");
            movementListPresenter = new MovementListPresenter(this, movementList);
            getSupportActionBar().setTitle(savedInstanceState.getString("toolbar"));
            emailTextView.setText(savedInstanceState.getString("email"));
            movementListPresenter.setSelectedMonth(savedInstanceState.getString("selectedMonth"));
            movementListPresenter.setSelectedYear(savedInstanceState.getString("selectedYear"));

            setYearList((YearList) savedInstanceState.getSerializable("years"));

        }

        movementAdapter.setMovementListListener(movementListPresenter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                movementListPresenter.refreshMovementList();
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        logoutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }


    @Override
    protected void onSaveInstanceState(Bundle bundle) {

        super.onSaveInstanceState(bundle);

        if (movementAdapter != null) {

            bundle.putSerializable("movements", new MovementList(movementAdapter.getMovementList()));
            bundle.putString("toolbar", getSupportActionBar().getTitle().toString());

            SharedPreferences sharedPref = getSharedPreferences(getString(R.string.app_full_name), Context.MODE_PRIVATE);
            bundle.putString("email", sharedPref.getString(getString(R.string.email), ""));

            bundle.putString("selectedMonth", movementListPresenter.getSelectedMonth());
            bundle.putString("selectedYear", movementListPresenter.getSelectedYear());

        }

        if (yearList != null) {
            bundle.putSerializable("years", yearList);
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
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showError(String error) {

        new MaterialDialog.Builder(this)
                .title(getString(R.string.error_oops))
                .content(error)
                .positiveText(R.string.dialog_close)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        dialog.dismiss();
                        hideError();
                    }

                })
                .show();
    }

    @Override
    public void hideError() {
        hideLoading();
        movementListPresenter.hideError();
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
    public void appendMovement(Movement movement) {
        movementAdapter.addMovement(movement);
        movementListPresenter.updateTitle(movementAdapter.getMovementList());
    }

    @Override
    public void deleteMovement(Movement movement) {
        movementAdapter.removeMovement(movement);
        movementListPresenter.updateTitle(movementAdapter.getMovementList());
    }

    @Override
    public void updateMovement(Movement movement) {
        movementAdapter.updateMovement(movement);
        movementListPresenter.updateTitle(movementAdapter.getMovementList());
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
    public void startEditMovementActivity(boolean isOutput, Movement movement) {
        Intent intent = new Intent(this, EditMovementActivity.class);
        if (movement == null) {
            intent.putExtra("isOutput", isOutput);
            intent.putExtra("action", "add");
        } else {
            intent.putExtra("isOutput", isOutput);
            intent.putExtra("action", "update");
            intent.putExtra("movement", movement);
        }

        startActivityForResult(intent, 1);
    }

    @Override
    public void setEmail(String email) {
        emailTextView.setText(email);
    }

    @Override
    public void logout() {
        movementListPresenter.logout();
    }

    @Override
    public void startLoginActivity() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void showDialog() {
        progressDialog = new MaterialDialog.Builder(this)
                .title(getString(R.string.dialog_deleting))
                .content(getString(R.string.dialog_wait))
                .progress(true, 0)
                .show();
    }

    @Override
    public void hideDialog() {
        progressDialog.dismiss();
    }

    @Override
    public void showDeleteDialog(final Movement movement) {
        new MaterialDialog.Builder(this)
                .title(R.string.dialog_delete_title)
                .content("Voules-vous supprimer le mouvement " + movement.getName() + " ?")
                .positiveText(R.string.dialog_delete_agree)
                .negativeText(R.string.dialog_delete_disagree)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        movementListPresenter.deleteMovement(movement);
                        dialog.dismiss();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void setYearList(YearList yearList) {
        this.yearList = yearList;

        String[] years = new String[yearList.getYearList().size()];
        int i = 0;

        for (Year year : yearList.getYearList()) {
            years[i] = year.getYear();
            i++;
        }


        ArrayAdapter<String> adapterYear = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years);
        adapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(adapterYear);
    }

    @Override
    public boolean isYearListEmpty() {
        if (yearList == null || yearList.getYearList().size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {

            if(resultCode == RESULT_OK) {
                if (data.getStringExtra("action").equals("add")) {
                    movementListPresenter.onMovementSaved((Movement) data.getSerializableExtra("movement"));
                } else {
                    movementListPresenter.onMovementUpdated((Movement) data.getSerializableExtra("movement"));
                }


            }
        }
    }


    @Override
    public Context getContext() {
        return this;
    }
}
