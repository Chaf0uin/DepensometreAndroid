package com.kerboocorp.depensometre.domain.movement.impl;

import com.kerboocorp.depensometre.common.utils.BusProvider;
import com.kerboocorp.depensometre.domain.movement.SaveMovement;
import com.kerboocorp.depensometre.model.MovementDataSource;
import com.kerboocorp.depensometre.model.entities.Movement;
import com.kerboocorp.depensometre.model.entities.ResponseObject;
import com.kerboocorp.depensometre.model.entities.ResponseType;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

/**
 * Created by cgo on 9/04/2015.
 */
public class SaveMovementController implements SaveMovement {

    private final MovementDataSource movementDataSource;
    private final Bus uiBus;

    private String accessToken;
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

    @Subscribe
    @Override
    public void onMovementReceived(ResponseObject<Movement> response) {
        if (ResponseType.insert.equals(response.getType()) || ResponseType.update.equals(response.getType())) {
            sendMovementToPresenter(response);
        }
    }

    @Override
    public void setMovement(Movement movement) {
        this.movement = movement;
    }

    @Override
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public void saveMovement() {
        movementDataSource.saveMovement(accessToken, movement);
    }


    @Override
    public void sendMovementToPresenter(ResponseObject<Movement> response) {
        uiBus.post(response);
    }

    @Override
    public void unRegister() {
        BusProvider.getRestBusInstance().unregister(this);
    }

}
