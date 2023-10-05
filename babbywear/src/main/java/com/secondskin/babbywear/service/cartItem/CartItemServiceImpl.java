package com.secondskin.babbywear.service.cartItem;

import com.secondskin.babbywear.model.Cart;
import com.secondskin.babbywear.model.CartItem;

import com.secondskin.babbywear.repository.CartItemRepository;
import com.secondskin.babbywear.repository.CartRepository;
import com.secondskin.babbywear.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;



@Service
public class CartItemServiceImpl implements CartItemService {

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    UserService userService;

    @Override
    public List<CartItem> getCartItemsForUserCart(Cart cart) {
        return cartItemRepository.findByCart(cart);
    }

    @Override
    public CartItem save(CartItem cartItem) {
        return cartItemRepository.save(cartItem);
    }

    @Override
    public CartItem findById(Long cartItemId) {
        return cartItemRepository.findById(cartItemId).orElse(null);
    }



    @Override
    public float calculateCartTotal(Cart cart) {
        float cartTotal = 0.0f;
        if(cart.getCartItems()!=null) {
            for (CartItem cartItem : cart.getCartItems()) {
                float variantPrice = cartItem.getVariant().getPrice();
                int quantity = cartItem.getQuantity();
                float total = quantity * variantPrice;
                cartTotal += total;

            }
            return cartTotal;
        }
        else {
            throw new  CartItemNotFoundException("Not found");
        }

    }





}
