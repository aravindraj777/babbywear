package com.secondskin.babbywear.service.cartItem;

public class CartItemNotFoundException extends RuntimeException{
    public CartItemNotFoundException(String message) {
        super(message);
    }
}
