package com.kerboocorp.depensometre.mvp.presenters;

import com.kerboocorp.depensometre.R;
import com.kerboocorp.depensometre.common.utils.BusProvider;
import com.kerboocorp.depensometre.domain.movement.impl.FindMovementListController;
import com.kerboocorp.depensometre.model.entities.Movement;
import com.kerboocorp.depensometre.model.entities.MovementList;
import com.kerboocorp.depensometre.model.rest.MovementRestSource;
import com.kerboocorp.depensometre.mvp.views.MovementListView;
import com.squareup.otto.Subscribe;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by cgo on 8/04/2015.
 */
public class MovementListPresenter extends Presenter {

    private final MovementListView movementListView;
    private FindMovementListController findMovementListController;

    private boolean isLoading = false;
    private boolean registered;

    public MovementListPresenter(MovementListView movementListView) {
        this.movementListView = movementListView;
        findMovementListController = new FindMovementListController(MovementRestSource.getInstance(), BusProvider.getUIBusInstance());
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
            String year = String.valueOf(cal.get(Calendar.YEAR));
            NumberFormat formatter = new DecimalFormat("00");
            String month = formatter.format(cal.get(Calendar.MONTH) + 1);
            findMovementListController.setMonth(month, year);
            findMovementListController.execute();

            int yearIndex = 0;
            for (int i = 0; i < movementListView.getContext().getResources().getStringArray(R.array.year).length; i++) {
                if (year.equals(movementListView.getContext().getResources().getStringArray(R.array.year)[i])) {
                    yearIndex = i;
                }
            }

            movementListView.setSelectedMonthSpinner(cal.get(Calendar.MONTH), yearIndex);

            movementListView.setTitle(movementListView.getContext().getResources().getStringArray(R.array.month)[cal.get(Calendar.MONTH)] + " " + year);

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

        String selectedMonth = formatter.format(month + 1);
        String selectedYear = movementListView.getContext().getResources().getStringArray(R.array.year)[year];

        findMovementListController.setMonth(selectedMonth, selectedYear);
        findMovementListController.execute();

        movementListView.setTitle(movementListView.getContext().getResources().getStringArray(R.array.month)[month] + " " + selectedYear);

    }
}
