package com.kerboocorp.depensometre.mvp.views;

/**
 * Created by cgo on 9/04/2015.
 */
public interface EditMovementView extends View {

    void cancel();

    void save();

    void setTitle(String title);

    void finish();

    void setDate(String date);

    void showDialog();

    void hideDialog();
}
