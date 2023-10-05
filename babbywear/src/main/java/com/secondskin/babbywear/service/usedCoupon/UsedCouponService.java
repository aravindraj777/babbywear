package com.secondskin.babbywear.service.usedCoupon;

import com.secondskin.babbywear.model.Coupon;
import com.secondskin.babbywear.model.UserInfo;

public interface UsedCouponService {

    void  allReadyUsedCoupon(UserInfo user, Coupon coupon);

    boolean checkUserUsedCoupon(UserInfo userInfo,Coupon coupon);
}
