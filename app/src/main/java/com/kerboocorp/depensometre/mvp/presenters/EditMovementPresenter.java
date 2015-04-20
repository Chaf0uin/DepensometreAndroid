package com.kerboocorp.depensometre.mvp.presenters;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ArrayAdapter;

import com.android.datetimepicker.date.DatePickerDialog;
import com.kerboocorp.depensometre.R;
import com.kerboocorp.depensometre.common.utils.BusProvider;
import com.kerboocorp.depensometre.domain.category.impl.FindCategoryListController;
import com.kerboocorp.depensometre.domain.movement.impl.SaveMovementController;
import com.kerboocorp.depensometre.domain.name.impl.FindNameListController;
import com.kerboocorp.depensometre.domain.session.impl.LoginController;
import com.kerboocorp.depensometre.model.entities.Category;
import com.kerboocorp.depensometre.model.entities.CategoryList;
import com.kerboocorp.depensometre.model.entities.Movement;
import com.kerboocorp.depensometre.model.entities.Name;
import com.kerboocorp.depensometre.model.entities.NameList;
import com.kerboocorp.depensometre.model.entities.ResponseError;
import com.kerboocorp.depensometre.model.entities.ResponseObject;
import com.kerboocorp.depensometre.model.entities.ResponseType;
import com.kerboocorp.depensometre.model.rest.CategoryRestSource;
import com.kerboocorp.depensometre.model.rest.MovementRestSource;
import com.kerboocorp.depensometre.model.rest.NameRestSource;
import com.kerboocorp.depensometre.model.rest.SessionRestSource;
import com.kerboocorp.depensometre.mvp.views.EditMovementView;
import com.kerboocorp.depensometre.mvp.views.LoginView;
import com.kerboocorp.depensometre.utils.ConnectionDetector;
import com.squareup.otto.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by cgo on 9/04/2015.
 */
public class EditMovementPresenter extends Presenter implements DatePickerDialog.OnDateSetListener {

    private final EditMovementView editMovementView;
    private SaveMovementController saveMovementController;
    private FindCategoryListController findCategoryListController;
    private FindNameListController findNameListController;

    private boolean isLoading = false;
    private boolean registered;

    private boolean movementType;

    private SimpleDateFormat dateFormat;
    private Calendar calendar;

    private Movement currentMovement;

    public EditMovementPresenter(EditMovementView editMovementView) {
        this.editMovementView = editMovementView;
        saveMovementController = new SaveMovementController(MovementRestSource.getInstance(), BusProvider.getUIBusInstance());
        findCategoryListController = new FindCategoryListController(CategoryRestSource.getInstance(), BusProvider.getUIBusInstance());
        findNameListController = new FindNameListController(NameRestSource.getInstance(), BusProvider.getUIBusInstance());

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        editMovementView.setDate(dateFormat.format(calendar.getTime()));

        currentMovement = new Movement();
    }

    public void saveMovement(String name, String category, String amount) {

        if (!ConnectionDetector.getInstance().isConnected(editMovementView.getContext())) {
            editMovementView.showError(editMovementView.getContext().getString(R.string.error_no_connection));
        } else {
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
    }

    @Override
    public void start() {
        if (!registered) {
            BusProvider.getUIBusInstance().register(this);
            registered = true;
        }

        if (ConnectionDetector.getInstance().isConnected(editMovementView.getContext())) {
            SharedPreferences sharedPref = editMovementView.getContext().getSharedPreferences(editMovementView.getContext().getString(R.string.app_full_name), Context.MODE_PRIVATE);
            String accessToken = sharedPref.getString(editMovementView.getContext().getString(R.string.access_token), "");

            findCategoryListController.setAccessToken(accessToken);
            findCategoryListController.execute();

            findNameListController.setAccessToken(accessToken);
            findNameListController.execute();
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
        currentMovement.setMovementType(movementType);
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

    public Movement getCurrentMovement() {
        return currentMovement;
    }

    @Subscribe
    public void onCategoryListReceived(CategoryList categoryList) {

        List<String> categories = new ArrayList<String>();

        for (Category category : categoryList.getCategoryList()) {
            if (category.getCategory() != null) {
                categories.add(category.getCategory());
            }
        }

        String[] categoryArray = categories.toArray(new String[categories.size()]);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(editMovementView.getContext(),
                android.R.layout.simple_dropdown_item_1line, categoryArray);

        editMovementView.setCategoryies(adapter);

    }

    @Subscribe
    public void onNameListReceived(NameList nameList) {

        List<String> names = new ArrayList<String>();

        for (Name name : nameList.getNameList()) {
            if (name.getName() != null) {
                names.add(name.getName());
            }
        }

        String[] nameArray = names.toArray(new String[names.size()]);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(editMovementView.getContext(),
                android.R.layout.simple_dropdown_item_1line, nameArray);

        editMovementView.setNames(adapter);
    }

    @Subscribe
    public void onErrorReceived(ResponseError error) {
        editMovementView.hideDialog();
        if (ResponseType.insert.equals(error.getType())) {
            editMovementView.showError(editMovementView.getContext().getString(R.string.error_insert_movement));
        } else if (ResponseType.update.equals(error.getType())) {
            editMovementView.showError(editMovementView.getContext().getString(R.string.error_update_movement));
        }
    }
}
