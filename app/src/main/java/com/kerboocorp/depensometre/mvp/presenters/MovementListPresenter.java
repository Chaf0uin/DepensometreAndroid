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
    private boolean registered;

    private String selectedMonth;
    private String selectedYear;

    public MovementListPresenter(MovementListView movementListView) {
        this.movementListView = movementListView;
        findMovementListController = new FindMovementListController(MovementRestSource.getInstance(), BusProvider.getUIBusInstance());
        deleteMovementController = new DeleteMovementController(MovementRestSource.getInstance(), BusProvider.getUIBusInstance());
    }

    @Subscribe
    public void onMovementListReceived(MovementList movementList) {

        movementListView.hideLoading();

        if (movementListView.isMovementListEmpty()) {
            movementListView.showMovementList(movementList);
        } else {
            movementListView.appendMovementList(movementList);
        }

        isLoading = false;
    }

    @Override
    public void start() {
        if (movementListView.isMovementListEmpty()) {

            BusProvider.getUIBusInstance().register(this);
            registered = true;

            movementListView.showLoading();

            Date now = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(now);
            selectedYear = String.valueOf(cal.get(Calendar.YEAR));
            NumberFormat formatter = new DecimalFormat("00");
            selectedMonth = formatter.format(cal.get(Calendar.MONTH) + 1);

            SharedPreferences sharedPref = movementListView.getContext().getSharedPreferences(movementListView.getContext().getString(R.string.app_full_name), Context.MODE_PRIVATE);
            String accessToken = sharedPref.getString(movementListView.getContext().getString(R.string.access_token), "");

            findMovementListController.setAccessToken(accessToken);
            findMovementListController.setMonth(selectedMonth, selectedYear);
            findMovementListController.execute();

            int yearIndex = 0;
            for (int i = 0; i < movementListView.getContext().getResources().getStringArray(R.array.year).length; i++) {
                if (selectedYear.equals(movementListView.getContext().getResources().getStringArray(R.array.year)[i])) {
                    yearIndex = i;
                }
            }

            movementListView.setSelectedMonthSpinner(cal.get(Calendar.MONTH), yearIndex);

            movementListView.setTitle(movementListView.getContext().getResources().getStringArray(R.array.month)[cal.get(Calendar.MONTH)] + " " + selectedYear);

            movementListView.setEmail(sharedPref.getString(movementListView.getContext().getString(R.string.email), ""));
        }
    }

    @Override
    public void stop() {
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

        findMovementListController.setMonth(selectedMonth, selectedYear);
        findMovementListController.execute();

        movementListView.setTitle(movementListView.getContext().getResources().getStringArray(R.array.month)[month] + " " + selectedYear);

    }

    public void refreshMovementList() {
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
}
