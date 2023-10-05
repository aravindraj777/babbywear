package com.secondskin.babbywear.service.cartItem;

import com.secondskin.babbywear.model.Cart;
import com.secondskin.babbywear.model.CartItem;

import java.util.List;

public interface CartItemService {


    List<CartItem> getCartItemsForUserCart(Cart cart);

    CartItem save(CartItem cartItem);

    CartItem findById(Long cartItemId);

//    float calculateTotalPrice(Long cartItemId, int quantity);

    float calculateCartTotal(Cart cart);


//    List<CartItem> getCartItemsForUser(String userName);
}
