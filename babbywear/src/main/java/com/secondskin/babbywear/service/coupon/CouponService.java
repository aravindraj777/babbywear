package com.secondskin.babbywear.service.coupon;

import com.secondskin.babbywear.model.Coupon;
import com.secondskin.babbywear.model.CouponType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface CouponService {

    List<Coupon> getAllCoupon();

    Coupon createCoupon(Coupon coupon);

    List<CouponType> getAllCouponTypes();

    Coupon getCouponByCode(String couponCode);

    float calculateDiscount(float cartTotal,Coupon coupon);

    void updateCoupon(Coupon coupon);

    List<Coupon> getAvailableCoupons(float cartTotal,Long id);
}
