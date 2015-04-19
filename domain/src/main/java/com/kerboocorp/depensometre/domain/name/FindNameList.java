package com.kerboocorp.depensometre.domain.name;

import com.kerboocorp.depensometre.domain.UseCase;
import com.kerboocorp.depensometre.model.entities.NameList;
import com.kerboocorp.depensometre.model.entities.ResponseError;

/**
 * Created by chris on 17/04/15.
 */
public interface FindNameList extends UseCase {

    public void onNameListReceived(NameList response);

    public void setAccessToken(String accessToken);

    public void requestNameList();

    public void sendNameListToPresenter(NameList response);

    public void unRegister();
}
