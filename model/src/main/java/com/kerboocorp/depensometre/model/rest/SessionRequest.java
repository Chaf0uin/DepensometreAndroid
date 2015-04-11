package com.kerboocorp.depensometre.model.rest;

/**
 * Created by chris on 9/04/15.
 */
public class SessionRequest {
    final String email;
    final String password;

    public SessionRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
