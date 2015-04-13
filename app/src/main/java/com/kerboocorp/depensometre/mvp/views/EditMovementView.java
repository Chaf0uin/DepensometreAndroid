package com.kerboocorp.depensometre.mvp.views;

import com.kerboocorp.depensometre.model.entities.Movement;

/**
 * Created by cgo on 9/04/2015.
 */
public interface EditMovementView extends View {

    void cancel();

    void save();

    void setTitle(String title);

    void finish(Movement movement, String action);

    void setDate(String date);

    void showDialog();

    void hideDialog();

    void fillForm(Movement movement);

}
