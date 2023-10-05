package com.secondskin.babbywear.service.wishlistItem;

import com.secondskin.babbywear.model.WishListItem;
import com.secondskin.babbywear.model.Wishlist;
import com.secondskin.babbywear.repository.WishListItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishlistItemServiceImpl implements WishlistItemService{

    @Autowired
    WishListItemRepository wishListItemRepository;


    @Override
    public List<WishListItem> getWishlistItems(Wishlist wishlist) {
        return wishListItemRepository.findByWishlist(wishlist);
    }
}
