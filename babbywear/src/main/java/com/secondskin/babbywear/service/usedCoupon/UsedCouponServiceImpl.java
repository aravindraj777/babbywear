package com.secondskin.babbywear.service.usedCoupon;

import com.secondskin.babbywear.model.Coupon;
import com.secondskin.babbywear.model.UsedCoupons;
import com.secondskin.babbywear.model.UserInfo;
import com.secondskin.babbywear.repository.UsedCouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsedCouponServiceImpl implements  UsedCouponService{

    @Autowired
    UsedCouponRepository usedCouponRepository;


    @Override
    public void allReadyUsedCoupon(UserInfo user, Coupon coupon) {

        UsedCoupons usedCoupons = new UsedCoupons();
        usedCoupons.setCoupon(coupon);
        usedCoupons.setUserInfo(user);
        usedCouponRepository.save(usedCoupons);
    }

    @Override
    public boolean checkUserUsedCoupon(UserInfo userInfo, Coupon coupon) {
        return usedCouponRepository.existsByUserInfoAndCoupon(userInfo,coupon);
    }


}
