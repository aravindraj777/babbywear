package com.secondskin.babbywear.service.cartItem;

import com.secondskin.babbywear.model.Cart;
import com.secondskin.babbywear.model.CartItem;
import com.secondskin.babbywear.model.UserInfo;
import com.secondskin.babbywear.repository.CartItemRepository;
import com.secondskin.babbywear.repository.CartRepository;
import com.secondskin.babbywear.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


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

//    @Override
//    public float calculateTotalPrice(Long cartItemId, int quantity) {
////        CartItem cartItem = cartItemRepository.findById(cartItemId)
////                .orElseThrow(()->new CartItemNotFoundException("CartItem not Found"));
////        float totalPrice =  cartItem.getVariant().getPrice() * quantity;
////        return totalPrice;
//
//        Optional<CartItem> cartItem  = cartItemRepository.findById(cartItemId);
//
//        if(cartItem.isPresent()){
//            CartItem cartItem1 = cartItem.get();
//            return cartItem1.getVariant().getPrice()*quantity;
//        }
//        else {
//            throw new CartItemNotFoundException("cartItem not found");
//        }
//    }

//    public float calculateTotalPrice(Long cartItemId, int quantity) {
//        System.out.println("kjabjkf");
//        CartItem cartItem = cartItemRepository.findById(cartItemId).orElse(null);
//
////        Optional<CartItem> cartItem = cartItemRepository.findById(cartItemId);
//
//        System.out.println("afjahkjl");
//
//        if(cartItem!=null){
//            float foundCartItem = cartItem.getVariant().getPrice()*quantity;
//             cartItem.setTotalPrice(foundCartItem);
//            cartItemRepository.save(cartItem);
//            return cartItem.getTotalPrice();
//        }
//        else {
//            throw  new RuntimeException();
//        }


//            System.out.println("abfajv");
//
//            cartItem.get().setTotalPrice(foundCartItem);


//            System.out.println("ajkfhakj");

//            return foundCartItem;
//    }

    @Override
    public float calculateCartTotal(Cart cart) {
        float cartTotal = 0.0f;
        if(cart.getCartItems()!=null) {
            for (CartItem cartItem : cart.getCartItems()) {
                float variantPrice = cartItem.getVariant().getPrice();
                int quantity = cartItem.getQuantity();
                float total = quantity * variantPrice;
                cartTotal += total;
                System.out.println(cartTotal);
            }
            return cartTotal;
        }
        else {
            throw new  CartItemNotFoundException("Not found");
        }

    }

//    @Override
//    public List<CartItem> getCartItemsForUser(String userName) {
//       Cart cart = cartRepository.findByUserInfo(userName);
//
//       if(cart!=null){
//           return cart.getCartItems();
//       }
//
//        return Collections.emptyList();
//    }




}
