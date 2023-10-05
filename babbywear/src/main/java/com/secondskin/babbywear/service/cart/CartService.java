package com.secondskin.babbywear.service.cart;

import com.secondskin.babbywear.model.Cart;
import com.secondskin.babbywear.model.CartItem;
import com.secondskin.babbywear.model.Variant;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CartService {

   ResponseEntity<String>  addToCart(String username, Long variantId);

   void addCart(Long userId,String name);

   double calculateUpdatedPrice(Long cartItemId, Integer newQuantity);

    void deleteCartItemById(long id);

    void saveCart(Cart cart);
    void deleteCart(Cart cart);



}
