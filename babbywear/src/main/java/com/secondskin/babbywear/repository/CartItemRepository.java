package com.secondskin.babbywear.repository;


import com.secondskin.babbywear.model.Cart;
import com.secondskin.babbywear.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long> {

    List<CartItem> findByCart(Cart cart);

     Optional<CartItem> findById(Long cartItemId);



}
