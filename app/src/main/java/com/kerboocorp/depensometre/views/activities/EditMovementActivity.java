package com.kerboocorp.depensometre.views.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.datetimepicker.date.DatePickerDialog;
import com.kerboocorp.depensometre.R;
import com.kerboocorp.depensometre.model.entities.Category;
import com.kerboocorp.depensometre.model.entities.CategoryList;
import com.kerboocorp.depensometre.model.entities.Movement;
import com.kerboocorp.depensometre.mvp.presenters.EditMovementPresenter;
import com.kerboocorp.depensometre.mvp.presenters.LoginPresenter;
import com.kerboocorp.depensometre.mvp.views.EditMovementView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

    private MaterialDialog progressDialog;

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

            Intent intent = getIntent();

            Movement currentMovement = (Movement)intent.getSerializableExtra("movement");
            if (currentMovement != null) {
                editMovementPresenter.setCurrentMovement(currentMovement);
                editMovementPresenter.setMovementType(intent.getBooleanExtra("isOutput", false), false);
            } else {
                editMovementPresenter.setMovementType(intent.getBooleanExtra("isOutput", false), true);

            }
        } else {
            editMovementPresenter = new EditMovementPresenter(this);

            Movement currentMovement = (Movement) savedInstanceState.getSerializable("movement");
            editMovementPresenter.setMovementType(currentMovement.getMovementType(), (currentMovement.getId() == null));
            editMovementPresenter.setCurrentMovement(currentMovement);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {

        super.onSaveInstanceState(bundle);

        Movement currentMovement = editMovementPresenter.getCurrentMovement();
        currentMovement.setName(nameEditText.getText().toString());
        currentMovement.setCategory(categoryEditText.getText().toString());
        currentMovement.setAmount(amountEditText.getText().toString());
        currentMovement.setDate(dateEditText.getText().toString());
        bundle.putSerializable("movement", currentMovement);

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
    protected void onStop() {

        super.onStop();
        editMovementPresenter.stop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        editMovementPresenter.start();
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
    public void finish(Movement movement, String action) {
        if (movement != null) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("action", action);
            returnIntent.putExtra("movement", movement);
            setResult(RESULT_OK, returnIntent);
        }

        finish();
    }

    @Override
    public void setDate(String date) {
        dateEditText.setText(date);
    }

    @Override
    public void showDialog() {
        progressDialog = new MaterialDialog.Builder(this)
                .title(getString(R.string.dialog_saving))
                .content(getString(R.string.dialog_wait))
                .progress(true, 0)
                .show();
    }

    @Override
    public void hideDialog() {
        progressDialog.dismiss();
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
                    }

                })
                .show();
    }

    @Override
    public void fillForm(Movement movement) {
        nameEditText.setText(movement.getName());
        categoryEditText.setText(movement.getCategory());
        amountEditText.setText(movement.getAmount());


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat fullDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try {
            dateEditText.setText(simpleDateFormat.format(fullDateFormat.parse(movement.getDate())));
        } catch (ParseException e) {
            dateEditText.setText(simpleDateFormat.format(new Date()));
        }
    }

    @Override
    public void setCategoryies(ArrayAdapter<String> adapter) {
        categoryEditText.setAdapter(adapter);
    }


    @Override
    public void setNames(ArrayAdapter<String> adapter) {
        nameEditText.setAdapter(adapter);
    }


    @Override
    public Context getContext() {
        return this;
    }

}
