package com.kerboocorp.depensometre.model.entities;

import java.io.Serializable;

/**
 * Created by cgo on 7/04/2015.
 */
public class BaseEntity implements Serializable {

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
