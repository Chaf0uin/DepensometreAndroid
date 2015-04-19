package com.kerboocorp.depensometre.domain.name.impl;

import com.kerboocorp.depensometre.common.utils.BusProvider;
import com.kerboocorp.depensometre.domain.name.FindNameList;
import com.kerboocorp.depensometre.model.NameDataSource;
import com.kerboocorp.depensometre.model.entities.NameList;
import com.kerboocorp.depensometre.model.entities.ResponseError;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

/**
 * Created by chris on 17/04/15.
 */
public class FindNameListController implements FindNameList {

    private final NameDataSource nameDataSource;
    private final Bus uiBus;

    private String accessToken;

    public FindNameListController(NameDataSource nameDataSource, Bus uiBus) {
        if (nameDataSource == null)
            throw new IllegalArgumentException("NameDataSource cannot be null");

        if (uiBus == null)
            throw new IllegalArgumentException("Bus cannot be null");

        this.nameDataSource = nameDataSource;
        this.uiBus = uiBus;

        BusProvider.getRestBusInstance().register(this);
    }

    @Subscribe
    @Override
    public void onNameListReceived(NameList response) {
        sendNameListToPresenter(response);
    }

    @Override
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public void requestNameList() {
        nameDataSource.findNameList(accessToken);
    }

    @Override
    public void sendNameListToPresenter(NameList response) {
        uiBus.post(response);
    }

    @Override
    public void unRegister() {
        BusProvider.getRestBusInstance().unregister(this);
    }

    @Override
    public void execute() {
        requestNameList();
    }
}
