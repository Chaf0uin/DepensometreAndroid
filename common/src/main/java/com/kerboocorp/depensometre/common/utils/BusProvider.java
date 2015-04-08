package com.kerboocorp.depensometre.common.utils;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by cgo on 7/04/2015.
 */
public class BusProvider {

    private static final Bus REST_BUS = new Bus(ThreadEnforcer.ANY);
    private static final Bus UI_BUS = new Bus();

    private BusProvider() {};

    public static Bus getRestBusInstance() {

        return REST_BUS;
    }

    public static Bus getUIBusInstance () {

        return UI_BUS;
    }


}
