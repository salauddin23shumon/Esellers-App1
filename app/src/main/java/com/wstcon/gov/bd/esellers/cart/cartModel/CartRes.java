
package com.wstcon.gov.bd.esellers.cart.cartModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CartRes {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("order")
    @Expose
    private Order order;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

}
