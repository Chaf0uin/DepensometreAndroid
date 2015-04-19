package com.kerboocorp.depensometre.domain.category;

import com.kerboocorp.depensometre.domain.UseCase;
import com.kerboocorp.depensometre.model.entities.CategoryList;

/**
 * Created by chris on 17/04/15.
 */
public interface FindCategoryList extends UseCase {

    public void onCategoryListReceived(CategoryList response);

    public void setAccessToken(String accessToken);

    public void requestCategoryList();

    public void sendCategoryListToPresenter(CategoryList response);

    public void unRegister();
}
