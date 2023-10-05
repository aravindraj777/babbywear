package com.secondskin.babbywear.repository;

import com.secondskin.babbywear.model.WishListItem;
import com.secondskin.babbywear.model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishListItemRepository extends JpaRepository<WishListItem,Long> {

    List<WishListItem> findByWishlist(Wishlist wishlist);
}
