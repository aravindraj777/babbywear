package com.secondskin.babbywear.controller.shop;

public class UpdateQuantityResponse {

    private final float cartTotal;
    private final float cartItemTotal;

    public UpdateQuantityResponse(float cartTotal, float cartItemTotal) {
        this.cartTotal = cartTotal;
        this.cartItemTotal = cartItemTotal;
    }

    public float getCartTotal() {
        return cartTotal;
    }

    public float getCartItemTotal() {
        return cartItemTotal;
    }
}
