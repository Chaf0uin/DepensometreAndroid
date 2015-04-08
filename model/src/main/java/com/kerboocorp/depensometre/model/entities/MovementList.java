package com.kerboocorp.depensometre.model.entities;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cgo on 8/04/2015.
 */
public class MovementList implements Serializable {
    private List<Movement> movementList;

    public MovementList(List<Movement> movementList) {
        this.movementList = movementList;
    }

    public List<Movement> getMovementList() {
        return movementList;
    }

    public void setMovementList(List<Movement> movementList) {
        this.movementList = movementList;
    }
}
