package com.secondskin.babbywear.service.wishList;

import com.secondskin.babbywear.model.UserInfo;
import com.secondskin.babbywear.model.Variant;
import com.secondskin.babbywear.model.WishListItem;
import com.secondskin.babbywear.model.Wishlist;
import com.secondskin.babbywear.repository.UserRepository;
import com.secondskin.babbywear.repository.VariantRepository;
import com.secondskin.babbywear.repository.WishListRepository;
import com.secondskin.babbywear.service.variant.VariantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class WishListServiceImpl implements WishListService{



    @Autowired
    WishListRepository wishListRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    VariantRepository variantRepository;


    @Override
    public void addToWishList(String userName, Long variantId) {

        UserInfo userInfo = userRepository.findByUserName(userName).orElseThrow(()->new
                UsernameNotFoundException("user Not Found"));

        Variant variant = variantRepository.findById(variantId).orElseThrow(()->new
                VariantNotFoundException("variant Not Exists"));


        Wishlist wishlist = userInfo.getWishlist();


            if(wishlist == null){
                wishlist = new Wishlist();
                wishlist.setUserInfo(userInfo);
                userInfo.setWishlist(wishlist);

            }
            boolean variantAlreadyInWishList =  wishlist.getWishListItems().stream().
                    anyMatch(wishListItem -> wishListItem.getVariant().equals(variant));

        if (!variantAlreadyInWishList){
            WishListItem wishListItem = new WishListItem();
            wishListItem.setVariant(variant);
            wishListItem.setWishlist(wishlist);
            wishlist.getWishListItems().add(wishListItem);
            wishListRepository.save(wishlist);
        }
    }
}
