package com.kerboocorp.depensometre.mvp.presenters;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.datetimepicker.date.DatePickerDialog;
import com.kerboocorp.depensometre.R;
import com.kerboocorp.depensometre.common.utils.BusProvider;
import com.kerboocorp.depensometre.domain.movement.impl.SaveMovementController;
import com.kerboocorp.depensometre.domain.session.impl.LoginController;
import com.kerboocorp.depensometre.model.entities.Movement;
import com.kerboocorp.depensometre.model.entities.ResponseObject;
import com.kerboocorp.depensometre.model.entities.ResponseType;
import com.kerboocorp.depensometre.model.rest.MovementRestSource;
import com.kerboocorp.depensometre.model.rest.SessionRestSource;
import com.kerboocorp.depensometre.mvp.views.EditMovementView;
import com.kerboocorp.depensometre.mvp.views.LoginView;
import com.squareup.otto.Subscribe;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by cgo on 9/04/2015.
 */
public class EditMovementPresenter extends Presenter implements DatePickerDialog.OnDateSetListener {

    private final EditMovementView editMovementView;
    private SaveMovementController saveMovementController;

    private boolean isLoading = false;
    private boolean registered;

    private boolean movementType;

    private SimpleDateFormat dateFormat;
    private Calendar calendar;

    private Movement currentMovement;

    public EditMovementPresenter(EditMovementView editMovementView) {
        this.editMovementView = editMovementView;
        saveMovementController = new SaveMovementController(MovementRestSource.getInstance(), BusProvider.getUIBusInstance());

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        editMovementView.setDate(dateFormat.format(calendar.getTime()));

        currentMovement = new Movement();
    }

    public void saveMovement(String name, String category, String amount) {
        editMovementView.showDialog();

        currentMovement.setName(name);
        currentMovement.setCategory(category);
        currentMovement.setAmount(amount);
        currentMovement.setMovementType(movementType);
        currentMovement.setDate(dateFormat.format(calendar.getTime()));
        saveMovementController.setMovement(currentMovement);

        SharedPreferences sharedPref = editMovementView.getContext().getSharedPreferences(editMovementView.getContext().getString(R.string.app_full_name), Context.MODE_PRIVATE);
        String accessToken = sharedPref.getString(editMovementView.getContext().getString(R.string.access_token), "");
        saveMovementController.setAccessToken(accessToken);

        saveMovementController.execute();
    }

    @Override
    public void start() {
        if (!registered) {
            BusProvider.getUIBusInstance().register(this);
            registered = true;
        }
    }

    @Override
    public void stop() {
        if (registered) {
            BusProvider.getUIBusInstance().unregister(this);
            registered = false;
        }
    }

    public void setMovementType(boolean movementType, boolean isNew) {
        this.movementType = movementType;
        if (isNew) {
            if (movementType) {
                editMovementView.setTitle("Ajouter dépense");
            } else {
                editMovementView.setTitle("Ajouter rentrée");
            }
        } else {
            if (movementType) {
                editMovementView.setTitle("Editer dépense");
            } else {
                editMovementView.setTitle("Editer rentrée");
            }
        }

    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int monthOfYear, int dayOfMonth) {
        calendar.set(year, monthOfYear, dayOfMonth);
        editMovementView.setDate(dateFormat.format(calendar.getTime()));
    }

    public Calendar getCalendar() {
        return calendar;
    }

    @Subscribe
    public void onMovementReceived(ResponseObject<Movement> response) {
        if (ResponseType.insert.equals(response.getType())) {
            editMovementView.hideDialog();
            editMovementView.finish(response.getContent(), "add");
        } else if (ResponseType.update.equals(response.getType())) {
            editMovementView.hideDialog();
            editMovementView.finish(response.getContent(), "update");
        }

    }

    public void setCurrentMovement(Movement currentMovement) {
        this.currentMovement = currentMovement;
        editMovementView.fillForm(currentMovement);
    }
}
