package com.kerboocorp.depensometre.domain.movement;

import com.kerboocorp.depensometre.domain.UseCase;
import com.kerboocorp.depensometre.model.entities.Movement;
import com.kerboocorp.depensometre.model.entities.ResponseError;
import com.kerboocorp.depensometre.model.entities.ResponseObject;

/**
 * Created by chris on 12/04/15.
 */
public interface DeleteMovement extends UseCase {

    public void onMovementDeleted(ResponseObject<Movement> response);

    public void setMovement(Movement movement);

    public void setAccessToken(String accessToken);

    public void deleteMovement();

    public void sendMovementToPresenter(ResponseObject<Movement> response);

    public void register();

    public void unRegister();

    public void onErrorReceived(ResponseError error);

    public void sendErrorToPresenter(ResponseError error);
}
