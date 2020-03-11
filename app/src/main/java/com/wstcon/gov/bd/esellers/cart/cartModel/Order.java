
package com.wstcon.gov.bd.esellers.cart.cartModel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Order {

    @SerializedName("customer_id")
    @Expose
    private Integer customerId;
    @SerializedName("order_total")
    @Expose
    private Integer orderTotal;
    @SerializedName("payment_type")
    @Expose
    private String paymentType;
    @SerializedName("cart")
    @Expose
    private List<Cart> cart = null;
    @SerializedName("address")
    @Expose
    private Address address;
    @SerializedName("has_different_shipping")
    @Expose
    private Integer hasDifferentShipping;

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(Integer orderTotal) {
        this.orderTotal = orderTotal;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public List<Cart> getCart() {
        return cart;
    }

    public void setCart(List<Cart> cart) {
        this.cart = cart;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Integer getHasDifferentShipping() {
        return hasDifferentShipping;
    }

    public void setHasDifferentShipping(Integer hasDifferentShipping) {
        this.hasDifferentShipping = hasDifferentShipping;
    }

}
