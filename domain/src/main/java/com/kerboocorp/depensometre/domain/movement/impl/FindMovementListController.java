package com.kerboocorp.depensometre.domain.movement.impl;

import com.kerboocorp.depensometre.common.utils.BusProvider;
import com.kerboocorp.depensometre.domain.movement.FindMovementList;
import com.kerboocorp.depensometre.model.entities.Movement;
import com.kerboocorp.depensometre.model.entities.MovementList;
import com.kerboocorp.depensometre.model.rest.MovementRestSource;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.List;

/**
 * Created by cgo on 8/04/2015.
 */
public class FindMovementListController implements FindMovementList {

    private final MovementRestSource movementRestSource;
    private final Bus uiBus;

    public FindMovementListController(MovementRestSource movementRestSource, Bus uiBus) {
        if (movementRestSource == null)
            throw new IllegalArgumentException("MovementRestSource cannot be null");

        if (uiBus == null)
            throw new IllegalArgumentException("Bus cannot be null");

        this.movementRestSource = movementRestSource;
        this.uiBus = uiBus;

        BusProvider.getRestBusInstance().register(this);
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
    public void requestMovementList() {
        movementRestSource.findMovementList("", "");
    }

    @Override
    public void sendMovementListToPresenter(MovementList response) {
        uiBus.post(response);
    }

    @Override
    public void unRegister() {
        BusProvider.getRestBusInstance().unregister(this);
    }
}
