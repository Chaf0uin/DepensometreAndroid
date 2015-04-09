package com.kerboocorp.depensometre.model;

import com.kerboocorp.depensometre.model.entities.Movement;

/**
 * Created by cgo on 8/04/2015.
 */
public interface MovementDataSource {

    public void findMovementList(String year, String month);

    public void saveMovement(Movement movement);
}
