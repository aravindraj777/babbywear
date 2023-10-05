package com.secondskin.babbywear.service.cart;

import com.secondskin.babbywear.model.Cart;
import com.secondskin.babbywear.model.CartItem;
import com.secondskin.babbywear.model.UserInfo;
import com.secondskin.babbywear.model.Variant;
import com.secondskin.babbywear.repository.CartItemRepository;
import com.secondskin.babbywear.repository.CartRepository;
import com.secondskin.babbywear.repository.UserRepository;
import com.secondskin.babbywear.repository.VariantRepository;
import com.secondskin.babbywear.service.cartItem.CartItemNotFoundException;
import com.secondskin.babbywear.service.user.UserService;
import com.secondskin.babbywear.service.variant.VariantNotFoundException;
import com.secondskin.babbywear.service.variant.VariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService{

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    VariantService variantService;

    @Autowired
    VariantRepository variantRepository;




    @Override

    public ResponseEntity<String> addToCart(String username, Long variantId) {

        UserInfo userInfo = userRepository.findByUserName(username)
                .orElseThrow(()-> new UsernameNotFoundException("user Not registered"));

        Variant variant = variantService.findById(variantId)
                .orElseThrow(()->new VariantNotFoundException("variant Not found"));

        Cart cart = userInfo.getCart();
        if(cart == null){
            cart = new Cart();
            cart.setUserInfo(userInfo);
            userInfo.setCart(cart);
            System.out.println("kjsadb");
        }

        boolean variantAlreadyInCart = cart.getCartItems().stream()
                .anyMatch(cartItem ->
                    cartItem.getVariant().equals(variant));

        if(!variantAlreadyInCart){
            CartItem cartItem  = new CartItem();
            cartItem.setVariant(variant);
            cartItem.setCart(cart);

            cartItem.setQuantity(1);
            cart.getCartItems().add(cartItem);
            cartRepository.save(cart);
            return ResponseEntity.ok("Variant added to Cart Successfully");

        }
        else {
            return ResponseEntity.badRequest().body("variant already in cart");
        }






    }

    @Override
    public void addCart(Long userId, String name) {
        System.out.println(name);

    }


    @Override
    public double calculateUpdatedPrice(Long cartItemId, Integer newQuantity) {

        Optional<CartItem> cartItemOptional = cartItemRepository.findById(cartItemId);
        if (cartItemOptional.isPresent()) {
            CartItem cartItem = cartItemOptional.get();
            // Update the cart item's quantity and recalculate the price
            cartItem.setQuantity(newQuantity);
            double updatedPrice = calculatePriceBasedOnQuantity(cartItem);
//            cartItem.setTotalPrice((float) updatedPrice);
            cartItemRepository.save(cartItem);
            return (float) updatedPrice;
        } else {
            throw new CartItemNotFoundException("Cart item not found with ID: " + cartItemId);
        }

    }

    @Override
    public void deleteCartItemById(long id) {
        cartItemRepository.deleteById(id);
    }

    @Override
    public void saveCart(Cart cart) {
        cartRepository.save(cart);
    }

    @Override
    public void deleteCart(Cart cart) {

        cartRepository.delete(cart);

    }


    private double calculatePriceBasedOnQuantity(CartItem cartItem) {
        // Fetch the variant associated with the cart item
        Variant variant = variantRepository.findById(cartItem.getVariant().getId()).orElse(null);

        if (variant != null) {
            // Calculate the price by multiplying variant's price with cart item's quantity
            return variant.getPrice() * cartItem.getQuantity();
        } else {
            throw new VariantNotFoundException("Variant not found for CartItem: " + cartItem.getId());
        }
    }




}
