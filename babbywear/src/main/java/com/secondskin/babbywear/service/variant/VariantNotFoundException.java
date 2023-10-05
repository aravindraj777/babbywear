package com.secondskin.babbywear.service.variant;

public class VariantNotFoundException extends RuntimeException{

    public VariantNotFoundException (String message){
        super(message);
    }
}
