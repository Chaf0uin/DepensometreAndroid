package com.kerboocorp.depensometre.views.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.android.datetimepicker.date.DatePickerDialog;
import com.kerboocorp.depensometre.R;
import com.kerboocorp.depensometre.mvp.presenters.EditMovementPresenter;
import com.kerboocorp.depensometre.mvp.presenters.LoginPresenter;
import com.kerboocorp.depensometre.mvp.views.EditMovementView;

import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by cgo on 9/04/2015.
 */
public class EditMovementActivity extends ActionBarActivity implements EditMovementView {

    private EditMovementPresenter editMovementPresenter;

    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    @InjectView(R.id.movementName)
    AutoCompleteTextView nameEditText;
    @InjectView(R.id.movementCategory)
    AutoCompleteTextView categoryEditText;
    @InjectView(R.id.movementAmount)
    EditText amountEditText;
    @InjectView(R.id.dateEditText)
    EditText dateEditText;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_movement);
        ButterKnife.inject(this);

        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = editMovementPresenter.getCalendar();
                DatePickerDialog.newInstance(editMovementPresenter, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show(getFragmentManager(), "datePicker");

            }
        });

        if (savedInstanceState == null) {
            editMovementPresenter = new EditMovementPresenter(this);
        } else {

        }

        Intent intent = getIntent();
        editMovementPresenter.setMovementType(intent.getBooleanExtra("isOutput", false));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_save:
                save();
                break;

        }


        return true;
    }

    @Override
    public void cancel() {

    }

    @Override
    public void save() {
        editMovementPresenter.saveMovement(
            nameEditText.getText().toString(),
            categoryEditText.getText().toString(),
            amountEditText.getText().toString()
        );
    }

    @Override
    public void setTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void setDate(String date) {
        dateEditText.setText(date);
    }

    @Override
    public void showDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.dialog_saving));
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    @Override
    public void hideDialog() {
        progressDialog.dismiss();
    }

    @Override
    public Context getContext() {
        return this;
    }

}
