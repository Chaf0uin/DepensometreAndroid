package com.kerboocorp.depensometre.domain.movement.impl;

import com.kerboocorp.depensometre.common.utils.BusProvider;
import com.kerboocorp.depensometre.domain.movement.DeleteMovement;
import com.kerboocorp.depensometre.model.MovementDataSource;
import com.kerboocorp.depensometre.model.entities.Movement;
import com.kerboocorp.depensometre.model.entities.ResponseError;
import com.kerboocorp.depensometre.model.entities.ResponseObject;
import com.kerboocorp.depensometre.model.entities.ResponseType;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

/**
 * Created by chris on 12/04/15.
 */
public class DeleteMovementController implements DeleteMovement {

    private final MovementDataSource movementDataSource;
    private final Bus uiBus;
    private boolean isRegistered;

    private String accessToken;
    private Movement movement;

    public DeleteMovementController(MovementDataSource movementDataSource, Bus uiBus) {
        if (movementDataSource == null)
            throw new IllegalArgumentException("MovementDataSource cannot be null");

        if (uiBus == null)
            throw new IllegalArgumentException("Bus cannot be null");

        this.movementDataSource = movementDataSource;
        this.uiBus = uiBus;

    }

    @Subscribe
    @Override
    public void onMovementDeleted(ResponseObject<Movement> response) {
        if (ResponseType.delete.equals(response.getType())) {
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
    public void deleteMovement() {
        movementDataSource.deleteMovement(accessToken, movement);
    }

    @Override
    public void sendMovementToPresenter(ResponseObject<Movement> response) {
        uiBus.post(response);
    }

    @Override
    public void register() {
        if (isRegistered) {
            BusProvider.getRestBusInstance().unregister(this);
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

    @Override
    public void onErrorReceived(ResponseError error) {
        sendErrorToPresenter(error);
    }

    @Override
    public void sendErrorToPresenter(ResponseError error) {
        uiBus.post(error);
    }

    @Override
    public void execute() {
        deleteMovement();
    }
}
