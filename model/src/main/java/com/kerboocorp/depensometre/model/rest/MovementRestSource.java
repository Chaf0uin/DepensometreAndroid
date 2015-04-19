package com.kerboocorp.depensometre.model.rest;

import com.kerboocorp.depensometre.common.utils.BusProvider;
import com.kerboocorp.depensometre.common.utils.Constants;
import com.kerboocorp.depensometre.model.MovementDataSource;
import com.kerboocorp.depensometre.model.entities.Movement;
import com.kerboocorp.depensometre.model.entities.MovementList;
import com.kerboocorp.depensometre.model.entities.ResponseError;
import com.kerboocorp.depensometre.model.entities.ResponseObject;
import com.kerboocorp.depensometre.model.entities.ResponseType;
import com.kerboocorp.depensometre.model.rest.api.MovementApi;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by cgo on 8/04/2015.
 */
public class MovementRestSource implements MovementDataSource {

    public static MovementRestSource INSTANCE;
    private final MovementApi movementApi;

    private MovementRestSource() {

        RestAdapter movementAPIRest = new RestAdapter.Builder()
                .setEndpoint(Constants.SERVICE_URL)
                .setLogLevel(RestAdapter.LogLevel.HEADERS_AND_ARGS)
                .build();

        movementApi = movementAPIRest.create(MovementApi.class);
    }

    public static MovementRestSource getInstance() {

        if (INSTANCE == null)
            INSTANCE = new MovementRestSource();

        return INSTANCE;
    }

    @Override
    public void findMovementList(String accessToken, String year, String month) {
        movementApi.findMovementList(accessToken, year, month, new Callback<List<Movement>>() {
            @Override
            public void success(List<Movement> movements, Response response) {
                BusProvider.getRestBusInstance().post(new MovementList(movements));
            }

            @Override
            public void failure(RetrofitError error) {
                System.out.printf("[DEBUG] MovementRestSource failure - " + error.getMessage());
                BusProvider.getRestBusInstance().post(new ResponseError(error, ResponseType.find));
            }
        });
    }

    @Override
    public void saveMovement(String accessToken, final Movement movement) {
        if (movement.getId() == null) {
            movementApi.insertMovement(accessToken, movement, new Callback<Movement>() {
                @Override
                public void success(Movement movement, Response response) {
                    BusProvider.getRestBusInstance().post(new ResponseObject(movement, ResponseType.insert));
                }

                @Override
                public void failure(RetrofitError error) {
                    System.out.printf("[DEBUG] MovementRestSource failure - " + error.getMessage());
                    BusProvider.getRestBusInstance().post(new ResponseError(error, ResponseType.insert));
                }
            });

        } else {
            movementApi.updateMovement(accessToken, movement.getId(), movement, new Callback<Boolean>() {
                @Override
                public void success(Boolean status, Response response) {
                    BusProvider.getRestBusInstance().post(new ResponseObject(movement, ResponseType.update));
                }

                @Override
                public void failure(RetrofitError error) {
                    System.out.printf("[DEBUG] MovementRestSource failure - " + error.getMessage());
                    BusProvider.getRestBusInstance().post(new ResponseError(error, ResponseType.insert));
                }
            });
        }
    }

    @Override
    public void deleteMovement(String accessToken, Movement movement) {
        movementApi.deleteMovement(accessToken, movement.getId(), new Callback<Movement>() {
            @Override
            public void success(Movement movement, Response response) {
                BusProvider.getRestBusInstance().post(new ResponseObject(movement, ResponseType.delete));
            }

            @Override
            public void failure(RetrofitError error) {
                System.out.printf("[DEBUG] MovementRestSource failure - " + error.getMessage());
                BusProvider.getRestBusInstance().post(new ResponseError(error, ResponseType.delete));
            }
        });
    }
}
