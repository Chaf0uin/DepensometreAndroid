package com.kerboocorp.depensometre.mvp.views;

import com.kerboocorp.depensometre.model.entities.Movement;
import com.kerboocorp.depensometre.model.entities.MovementList;

import java.util.List;

/**
 * Created by cgo on 8/04/2015.
 */
public interface MovementListView extends View {

    void showMovementList(MovementList movementList);

    void showLoading ();

    void hideLoading ();

    void showError (String error);

    void hideError ();

    boolean isMovementListEmpty ();

    void appendMovementList (MovementList movementList);
}
