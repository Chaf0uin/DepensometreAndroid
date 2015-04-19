package com.kerboocorp.depensometre.domain.movement.impl;

import com.kerboocorp.depensometre.common.utils.BusProvider;
import com.kerboocorp.depensometre.domain.movement.FindMovementList;
import com.kerboocorp.depensometre.model.MovementDataSource;
import com.kerboocorp.depensometre.model.entities.Movement;
import com.kerboocorp.depensometre.model.entities.MovementList;
import com.kerboocorp.depensometre.model.entities.ResponseError;
import com.kerboocorp.depensometre.model.rest.MovementRestSource;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.List;

/**
 * Created by cgo on 8/04/2015.
 */
public class FindMovementListController implements FindMovementList {

    private final MovementDataSource movementDataSource;
    private final Bus uiBus;
    private boolean isRegistered;

    private String accessToken;
    private String month;
    private String year;

    public FindMovementListController(MovementDataSource movementDataSource, Bus uiBus) {
        if (movementDataSource == null)
            throw new IllegalArgumentException("MovementDataSource cannot be null");

        if (uiBus == null)
            throw new IllegalArgumentException("Bus cannot be null");

        this.movementDataSource = movementDataSource;
        this.uiBus = uiBus;

    }

    @Override
    public void execute() {
        requestMovementList();
    }

    @Subscribe
    @Override
    public void onMovementListReceived(MovementList response) {
        sendMovementListToPresenter(response);
    }

    @Override
    public void setMonth(String month, String year) {
        this.month = month;
        this.year = year;
    }

    @Override
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public void requestMovementList() {
        movementDataSource.findMovementList(accessToken, year, month);
    }

    @Override
    public void sendMovementListToPresenter(MovementList response) {
        uiBus.post(response);
    }

    @Override
    public void register() {
        if (!isRegistered) {
            BusProvider.getRestBusInstance().register(this);
            isRegistered = true;
        }
    }

    @Override
    public void unRegister() {
        if (isRegistered) {
            BusProvider.getRestBusInstance().unregister(this);
            isRegistered = false;
        }

    }

    @Subscribe
    @Override
    public void onErrorReceived(ResponseError error) {
        sendErrorToPresenter(error);
    }

    @Override
    public void sendErrorToPresenter(ResponseError error) {
        uiBus.post(error);
    }
}
