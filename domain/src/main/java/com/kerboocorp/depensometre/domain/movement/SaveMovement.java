package com.kerboocorp.depensometre.domain.movement;

import com.kerboocorp.depensometre.domain.UseCase;
import com.kerboocorp.depensometre.model.entities.Movement;
import com.kerboocorp.depensometre.model.entities.ResponseError;
import com.kerboocorp.depensometre.model.entities.ResponseObject;

/**
 * Created by cgo on 9/04/2015.
 */
public interface SaveMovement extends UseCase {

    public void onMovementReceived(ResponseObject<Movement> response);

    public void setMovement(Movement movement);

    public void setAccessToken(String accessToken);

    public void saveMovement();

    public void sendMovementToPresenter(ResponseObject<Movement> response);

    public void unRegister();

    public void onErrorReceived(ResponseError error);

    public void sendErrorToPresenter(ResponseError error);
}
