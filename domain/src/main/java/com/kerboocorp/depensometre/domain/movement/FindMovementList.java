package com.kerboocorp.depensometre.domain.movement;

import com.kerboocorp.depensometre.domain.UseCase;
import com.kerboocorp.depensometre.model.entities.Movement;
import com.kerboocorp.depensometre.model.entities.MovementList;
import com.kerboocorp.depensometre.model.entities.ResponseError;

import java.util.List;

/**
 * Created by cgo on 8/04/2015.
 */
public interface FindMovementList extends UseCase {

    public void onMovementListReceived(MovementList response);

    public void setMonth(String month, String year);

    public void setAccessToken(String accessToken);

    public void requestMovementList();

    public void sendMovementListToPresenter(MovementList response);

    public void register();

    public void unRegister();

    public void onErrorReceived(ResponseError error);

    public void sendErrorToPresenter(ResponseError error);
}
