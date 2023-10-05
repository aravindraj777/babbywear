package com.secondskin.babbywear.service.order;

public class OrderNotFoundException extends RuntimeException {

    public  OrderNotFoundException(String message){
        super(message);
    }
}
