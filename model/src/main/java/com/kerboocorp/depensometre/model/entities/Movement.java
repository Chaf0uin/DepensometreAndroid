package com.kerboocorp.depensometre.model.entities;

import java.io.Serializable;

/**
 * Created by cgo on 7/04/2015.
 */
public class Movement extends BaseEntity implements Serializable {

    private String name;
    private String category;
    private String amount;
    private Boolean movementType;
    private String date;
    private String createdAt;
    private String updatedAt;
    private Integer userId;

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The category
     */
    public String getCategory() {
        return category;
    }

    /**
     *
     * @param category
     * The category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     *
     * @return
     * The amount
     */
    public String getAmount() {
        return amount;
    }

    /**
     *
     * @param amount
     * The amount
     */
    public void setAmount(String amount) {
        this.amount = amount;
    }

    /**
     *
     * @return
     * The movementType
     */
    public Boolean getMovementType() {
        return movementType;
    }

    /**
     *
     * @param movementType
     * The movementType
     */
    public void setMovementType(Boolean movementType) {
        this.movementType = movementType;
    }

    /**
     *
     * @return
     * The date
     */
    public String getDate() {
        return date;
    }

    /**
     *
     * @param date
     * The date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     *
     * @return
     * The createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     *
     * @param createdAt
     * The created_at
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     *
     * @return
     * The updatedAt
     */
    public String getUpdatedAt() {
        return updatedAt;
    }

    /**
     *
     * @param updatedAt
     * The updated_at
     */
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     *
     * @return
     * The userId
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     *
     * @param userId
     * The user_id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}
