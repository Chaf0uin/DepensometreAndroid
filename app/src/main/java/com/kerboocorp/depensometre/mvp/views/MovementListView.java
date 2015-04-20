package com.kerboocorp.depensometre.mvp.views;

import com.kerboocorp.depensometre.model.entities.Movement;
import com.kerboocorp.depensometre.model.entities.MovementList;
import com.kerboocorp.depensometre.model.entities.YearList;

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

    void appendMovement (Movement movement);

    void deleteMovement(Movement movement);

    void updateMovement(Movement movement);

    void selectMonth();

    void setTitle(String title);

    void closeDrawer();

    void setSelectedMonthSpinner(int month, int year);

    void startEditMovementActivity(boolean isOutput, Movement movement);

    void setEmail(String email);

    void logout();

    void startLoginActivity();

    void showDialog();

    void hideDialog();

    void showDeleteDialog(Movement movement);

    void setYearList(YearList yearList);

    boolean isYearListEmpty();
}
