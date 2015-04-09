package com.kerboocorp.depensometre.mvp.presenters;

import com.kerboocorp.depensometre.common.utils.BusProvider;
import com.kerboocorp.depensometre.domain.movement.impl.SaveMovementController;
import com.kerboocorp.depensometre.domain.session.impl.LoginController;
import com.kerboocorp.depensometre.model.rest.MovementRestSource;
import com.kerboocorp.depensometre.model.rest.SessionRestSource;
import com.kerboocorp.depensometre.mvp.views.EditMovementView;
import com.kerboocorp.depensometre.mvp.views.LoginView;

/**
 * Created by cgo on 9/04/2015.
 */
public class EditMovementPresenter extends Presenter {

    private final EditMovementView editMovementView;
    private SaveMovementController saveMovementController;

    private boolean isLoading = false;
    private boolean registered;

    public EditMovementPresenter(EditMovementView editMovementView) {
        this.editMovementView = editMovementView;
        saveMovementController = new SaveMovementController(MovementRestSource.getInstance(), BusProvider.getUIBusInstance());

    }

    public void saveMovement() {

    }

    @Override
    public void start() {
        if (!registered) {
            BusProvider.getUIBusInstance().register(this);
            registered = true;
        }
    }

    @Override
    public void stop() {
        if (registered) {
            BusProvider.getUIBusInstance().unregister(this);
            registered = false;
        }
    }
}
