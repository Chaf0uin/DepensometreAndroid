package com.kerboocorp.depensometre.domain.category.impl;

import com.kerboocorp.depensometre.common.utils.BusProvider;
import com.kerboocorp.depensometre.domain.category.FindCategoryList;
import com.kerboocorp.depensometre.model.CategoryDataSource;
import com.kerboocorp.depensometre.model.entities.CategoryList;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

/**
 * Created by chris on 17/04/15.
 */
public class FindCategoryListController implements FindCategoryList {

    private final CategoryDataSource categoryDataSource;
    private final Bus uiBus;

    private String accessToken;

    public FindCategoryListController(CategoryDataSource categoryDataSource, Bus uiBus) {
        if (categoryDataSource == null)
            throw new IllegalArgumentException("CategoryDataSource cannot be null");

        if (uiBus == null)
            throw new IllegalArgumentException("Bus cannot be null");

        this.categoryDataSource = categoryDataSource;
        this.uiBus = uiBus;

        BusProvider.getRestBusInstance().register(this);
    }

    @Subscribe
    @Override
    public void onCategoryListReceived(CategoryList response) {
        sendCategoryListToPresenter(response);
    }

    @Override
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public void requestCategoryList() {
        categoryDataSource.findCategoryList(accessToken);
    }

    @Override
    public void sendCategoryListToPresenter(CategoryList response) {
        uiBus.post(response);
    }

    @Override
    public void unRegister() {
        BusProvider.getRestBusInstance().unregister(this);
    }

    @Override
    public void execute() {
        requestCategoryList();
    }
}
