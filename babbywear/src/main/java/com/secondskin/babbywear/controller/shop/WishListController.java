package com.secondskin.babbywear.controller.shop;

import com.secondskin.babbywear.model.UserInfo;
import com.secondskin.babbywear.model.WishListItem;
import com.secondskin.babbywear.service.user.UserService;
import com.secondskin.babbywear.service.wishList.WishListService;
import com.secondskin.babbywear.service.wishlistItem.WishlistItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/wishlist")
public class WishListController {

    @Autowired
    WishListService wishListService;

    @Autowired
    UserService userService;

    @Autowired
    WishlistItemService wishlistItemService;


    @PostMapping("/add-to-wishlist/{variantId}")
    @ResponseBody
    public ResponseEntity<String> addToWishList(@PathVariable("variantId") Long variantId,
                                                Principal principal ){



        try {

            String user = principal.getName();


            wishListService.addToWishList(user,variantId);

            return ResponseEntity.ok("Item Added To Wishlist");


        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                    body("Failed to add to wishlist");
        }
    }

    @GetMapping("/view-wishlist")
    public String viewWishlist(Model model,Principal principal){

        String userName = principal.getName();
        UserInfo userInfo = userService.findByUsername(userName);

        List<WishListItem> wishListItems =  wishlistItemService.getWishlistItems(userInfo.getWishlist());

        model.addAttribute("wishlistItems",wishListItems);
        return "wishlist";
    }
}
