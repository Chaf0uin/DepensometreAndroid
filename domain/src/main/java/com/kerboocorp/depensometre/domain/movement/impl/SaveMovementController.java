package com.kerboocorp.depensometre.domain.movement.impl;

import com.kerboocorp.depensometre.common.utils.BusProvider;
import com.kerboocorp.depensometre.domain.movement.SaveMovement;
import com.kerboocorp.depensometre.model.MovementDataSource;
import com.kerboocorp.depensometre.model.entities.Movement;
import com.squareup.otto.Bus;

/**
 * Created by cgo on 9/04/2015.
 */
public class SaveMovementController implements SaveMovement {

    private final MovementDataSource movementDataSource;
    private final Bus uiBus;

    private Movement movement;

    public SaveMovementController(MovementDataSource movementDataSource, Bus uiBus) {
        if (movementDataSource == null)
            throw new IllegalArgumentException("MovementDataSource cannot be null");

        if (uiBus == null)
            throw new IllegalArgumentException("Bus cannot be null");

        this.movementDataSource = movementDataSource;
        this.uiBus = uiBus;

        BusProvider.getRestBusInstance().register(this);
    }

    @Override
    public void execute() {
        saveMovement();
    }

    @Override
    public void onMovementReceived(Movement response) {
        sendMovementToPresenter(response);
    }

    @Override
    public void setMovement(Movement movement) {
        this.movement = movement;
    }

    @Override
    public void saveMovement() {
        movementDataSource.saveMovement(movement);
    }


    @Override
    public void sendMovementToPresenter(Movement response) {
        uiBus.post(response);
    }

    @Override
    public void unRegister() {
        BusProvider.getRestBusInstance().unregister(this);
    }

}
