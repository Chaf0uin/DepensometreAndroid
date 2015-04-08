package com.kerboocorp.depensometre.mvp.presenters;

import com.kerboocorp.depensometre.common.utils.BusProvider;
import com.kerboocorp.depensometre.domain.movement.impl.FindMovementListController;
import com.kerboocorp.depensometre.model.entities.Movement;
import com.kerboocorp.depensometre.model.entities.MovementList;
import com.kerboocorp.depensometre.model.rest.MovementRestSource;
import com.kerboocorp.depensometre.mvp.views.MovementListView;
import com.squareup.otto.Subscribe;

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
            findMovementListController.execute();

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
}
