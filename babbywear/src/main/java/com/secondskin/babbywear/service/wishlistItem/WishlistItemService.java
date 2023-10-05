package com.secondskin.babbywear.service.wishlistItem;

import com.secondskin.babbywear.model.WishListItem;
import com.secondskin.babbywear.model.Wishlist;

import java.util.List;

public interface WishlistItemService {

    List<WishListItem> getWishlistItems(Wishlist wishlist);
}
