package com.secondskin.babbywear.service.cart;

import com.secondskin.babbywear.model.Cart;

import org.springframework.http.ResponseEntity;



public interface CartService {

   ResponseEntity<String>  addToCart(String username, Long variantId);



   double calculateUpdatedPrice(Long cartItemId, Integer newQuantity);

    void deleteCartItemById(long id);

    void saveCart(Cart cart);
    void deleteCart(Cart cart);



}
