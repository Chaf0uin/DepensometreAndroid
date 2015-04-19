package com.kerboocorp.depensometre.mvp.presenters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.kerboocorp.depensometre.R;
import com.kerboocorp.depensometre.common.utils.BusProvider;
import com.kerboocorp.depensometre.domain.movement.DeleteMovement;
import com.kerboocorp.depensometre.domain.movement.impl.DeleteMovementController;
import com.kerboocorp.depensometre.domain.movement.impl.FindMovementListController;
import com.kerboocorp.depensometre.model.entities.Movement;
import com.kerboocorp.depensometre.model.entities.MovementList;
import com.kerboocorp.depensometre.model.entities.ResponseError;
import com.kerboocorp.depensometre.model.entities.ResponseObject;
import com.kerboocorp.depensometre.model.entities.ResponseType;
import com.kerboocorp.depensometre.model.rest.MovementRestSource;
import com.kerboocorp.depensometre.mvp.views.MovementListView;
import com.kerboocorp.depensometre.utils.MovementListListener;
import com.squareup.otto.Subscribe;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by cgo on 8/04/2015.
 */
public class MovementListPresenter extends Presenter implements MovementListListener {

    private final MovementListView movementListView;
    private FindMovementListController findMovementListController;
    private DeleteMovementController deleteMovementController;

    private boolean isLoading = false;
    private boolean isRegistered;

    private String selectedMonth;
    private String selectedYear;

    private String titleDate;

    private boolean isErrorShown = false;

    public MovementListPresenter(MovementListView movementListView) {
        this.movementListView = movementListView;
        findMovementListController = new FindMovementListController(MovementRestSource.getInstance(), BusProvider.getUIBusInstance());
        deleteMovementController = new DeleteMovementController(MovementRestSource.getInstance(), BusProvider.getUIBusInstance());
    }

    public MovementListPresenter(MovementListView movementListView, MovementList movementList) {
        this.movementListView = movementListView;
        findMovementListController = new FindMovementListController(MovementRestSource.getInstance(), BusProvider.getUIBusInstance());
        deleteMovementController = new DeleteMovementController(MovementRestSource.getInstance(), BusProvider.getUIBusInstance());

        onMovementListReceived(movementList);
    }

    @Subscribe
    public void onMovementListReceived(MovementList movementList) {

        movementListView.hideLoading();

        if (movementListView.isMovementListEmpty()) {
            movementListView.showMovementList(movementList);
        } else {
            movementListView.appendMovementList(movementList);
        }

        double total = 0;

        for (Movement movement : movementList.getMovementList()) {
            if (movement.getMovementType()) {
                total -= Double.parseDouble(movement.getAmount());
            } else {
                total += Double.parseDouble(movement.getAmount());
            }
        }

        String formattedTotal;

        if (total > 0) {
            formattedTotal = "+" + String.format("%.2f", total);
        } else if (total < 0) {
            formattedTotal = String.format("%.2f", total);
        } else {
            formattedTotal = "0";
        }

        movementListView.setTitle(titleDate + " | " + formattedTotal + " €");

        isLoading = false;
    }

    @Override
    public void start() {
        if (!isRegistered) {
            BusProvider.getUIBusInstance().register(this);
            isRegistered = true;
        }

        findMovementListController.register();
        deleteMovementController.register();

        if (movementListView.isMovementListEmpty()) {

            movementListView.showLoading();

            Date now = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(now);
            selectedYear = String.valueOf(cal.get(Calendar.YEAR));
            NumberFormat formatter = new DecimalFormat("00");
            selectedMonth = formatter.format(cal.get(Calendar.MONTH) + 1);

            SharedPreferences sharedPref = movementListView.getContext().getSharedPreferences(movementListView.getContext().getString(R.string.app_full_name), Context.MODE_PRIVATE);
            String accessToken = sharedPref.getString(movementListView.getContext().getString(R.string.access_token), "");

            int yearIndex = 0;
            for (int i = 0; i < movementListView.getContext().getResources().getStringArray(R.array.year).length; i++) {
                if (selectedYear.equals(movementListView.getContext().getResources().getStringArray(R.array.year)[i])) {
                    yearIndex = i;
                }
            }

            movementListView.setSelectedMonthSpinner(cal.get(Calendar.MONTH), yearIndex);

            titleDate = movementListView.getContext().getResources().getStringArray(R.array.month)[cal.get(Calendar.MONTH)] + " " + selectedYear;
            movementListView.setTitle(titleDate);

            movementListView.setEmail(sharedPref.getString(movementListView.getContext().getString(R.string.email), ""));

            findMovementListController.setAccessToken(accessToken);
            findMovementListController.setMonth(selectedMonth, selectedYear);
            findMovementListController.execute();
        } else {
            titleDate = movementListView.getContext().getResources().getStringArray(R.array.month)[Integer.parseInt(selectedMonth)-1] + " " + selectedYear;
        }
    }

    @Override
    public void stop() {
        if (isRegistered) {
            BusProvider.getUIBusInstance().unregister(this);
            isRegistered = false;
        }

        findMovementListController.unRegister();
        deleteMovementController.unRegister();
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

    public void selectMonth(int month, int year) {
        movementListView.closeDrawer();
        movementListView.showLoading();

        NumberFormat formatter = new DecimalFormat("00");

        selectedMonth = formatter.format(month + 1);
        selectedYear = movementListView.getContext().getResources().getStringArray(R.array.year)[year];

        titleDate = movementListView.getContext().getResources().getStringArray(R.array.month)[month] + " " + selectedYear;
        movementListView.setTitle(titleDate);

        findMovementListController.setMonth(selectedMonth, selectedYear);
        findMovementListController.execute();

    }

    public void refreshMovementList() {
        SharedPreferences sharedPref = movementListView.getContext().getSharedPreferences(movementListView.getContext().getString(R.string.app_full_name), Context.MODE_PRIVATE);
        String accessToken = sharedPref.getString(movementListView.getContext().getString(R.string.access_token), "");
        findMovementListController.setAccessToken(accessToken);

        findMovementListController.setMonth(selectedMonth, selectedYear);
        findMovementListController.execute();
    }

    public void onMovementSaved(Movement movement) {
        Calendar calendar = Calendar.getInstance();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            calendar.setTime(formatter.parse(movement.getDate()));
        } catch (ParseException e) {
            calendar.setTime(new Date());
        }

        int movementMonth = calendar.get(Calendar.MONTH)+1;
        int movementYear = calendar.get(Calendar.YEAR);

        if ((movementMonth == Integer.valueOf(selectedMonth)) && movementYear == Integer.valueOf(selectedYear)) {
            movementListView.appendMovement(movement);
        }
    }

    public void onMovementUpdated(Movement movement) {
        movementListView.updateMovement(movement);

    }

    public void logout() {
        SharedPreferences sharedPref = movementListView.getContext().getSharedPreferences(movementListView.getContext().getString(R.string.app_full_name), Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.commit();

        movementListView.startLoginActivity();
    }

    @Override
    public void onClick(Movement movement) {
        movementListView.startEditMovementActivity(movement.getMovementType(), movement);
    }

    @Override
    public void onLongClick(Movement movement) {
        movementListView.showDeleteDialog(movement);
    }

    public void deleteMovement(Movement movement) {
        movementListView.showDialog();

        SharedPreferences sharedPref = movementListView.getContext().getSharedPreferences(movementListView.getContext().getString(R.string.app_full_name), Context.MODE_PRIVATE);
        String accessToken = sharedPref.getString(movementListView.getContext().getString(R.string.access_token), "");

        deleteMovementController.setAccessToken(accessToken);
        deleteMovementController.setMovement(movement);
        deleteMovementController.execute();
    }

    @Subscribe
    public void onMovementDeleted(ResponseObject<Movement> response) {

        if (ResponseType.delete.equals(response.getType())) {
            movementListView.hideLoading();

            movementListView.deleteMovement(response.getContent());

            movementListView.hideDialog();
        }

    }

    @Subscribe
    public void onErrorReceived(ResponseError error) {
        movementListView.hideLoading();

        if (!isErrorShown) {
            if (ResponseType.find.equals(error.getType())) {
                movementListView.showError(movementListView.getContext().getString(R.string.error_find_movements));
            } else if (ResponseType.delete.equals(error.getType())) {
                movementListView.showError(movementListView.getContext().getString(R.string.error_delete_movement));
            }

            isErrorShown = true;
        }
    }

    public void hideError() {
        isErrorShown = false;
    }

    public String getSelectedYear() {
        return selectedYear;
    }

    public void setSelectedYear(String selectedYear) {
        this.selectedYear = selectedYear;
    }

    public String getSelectedMonth() {
        return selectedMonth;
    }

    public void setSelectedMonth(String selectedMonth) {
        this.selectedMonth = selectedMonth;
    }

    public void updateTitle(List<Movement> movementList) {

        double total = 0;

        for (Movement movement : movementList) {
            if (movement.getMovementType()) {
                total -= Double.parseDouble(movement.getAmount());
            } else {
                total += Double.parseDouble(movement.getAmount());
            }
        }

        String formattedTotal;

        if (total > 0) {
            formattedTotal = "+" + String.format("%.2f", total);
        } else if (total < 0) {
            formattedTotal = String.format("%.2f", total);
        } else {
            formattedTotal = "0";
        }

        movementListView.setTitle(titleDate + " | " + formattedTotal + " €");
    }
}
