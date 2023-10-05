package com.secondskin.babbywear.controller.admin;


import com.secondskin.babbywear.model.Coupon;

import com.secondskin.babbywear.model.UserInfo;

import com.secondskin.babbywear.service.cartItem.CartItemService;
import com.secondskin.babbywear.service.coupon.CouponService;
import com.secondskin.babbywear.service.usedCoupon.UsedCouponService;
import com.secondskin.babbywear.service.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;


@Controller
@RequestMapping("/coupon")
public class CouponController {

    @Autowired
    UserService userService;



    @Autowired
    CouponService couponService;

    @Autowired
    CartItemService cartItemService;

    @Autowired
    UsedCouponService usedCouponService;


    @GetMapping("/create")
    public String coupon(Model model){
        model.addAttribute("coupon",new Coupon());
        List<Coupon> coupons = couponService.getAllCoupon();






        model.addAttribute("coupons",coupons);
        return "admin/coupon-list";
    }

    @PostMapping("/create")
    public String createCoupon(@ModelAttribute  Coupon coupon ){


        couponService.createCoupon(coupon);
        return "redirect:/coupon/create";
    }




    @PostMapping("/applyCoupon")
    public String applyCoupon(@ModelAttribute("couponCode") String couponCode, Principal principal,
                              Model model, HttpSession session){


        UserInfo user =  userService.findByUsername(principal.getName());
        float totalCartAmount = cartItemService.calculateCartTotal(user.getCart());


        Coupon coupon = couponService.getCouponByCode(couponCode);

        LocalDateTime today = LocalDateTime.now();
        LocalDateTime expirationDate = coupon.getExpirationDate();
        boolean usedCoupon = usedCouponService.checkUserUsedCoupon(user,coupon);

        if(coupon != null && !expirationDate.isBefore(today) && !usedCoupon){
            if(totalCartAmount >=coupon.getMinimum() && totalCartAmount <=coupon.getMaximum()) {





                float cartTotal = totalCartAmount;
                float discount = couponService.calculateDiscount(cartTotal, coupon);

                float updatedCartTotal = cartTotal - discount;

                session.setAttribute("couponApplied",true);
                session.setAttribute("updatedCartTotal",updatedCartTotal);
                usedCouponService.allReadyUsedCoupon(user,coupon);
                coupon.setStock(coupon.getStock() -1);
                couponService.updateCoupon(coupon);

            }
            else {
                model.addAttribute("error", "Cart total is not within the coupon's applicable range.");
            }


        }
        else {

            model.addAttribute("error", "Invalid or expired coupon code.");
        }
        return "redirect:/checkout";


    }








}
