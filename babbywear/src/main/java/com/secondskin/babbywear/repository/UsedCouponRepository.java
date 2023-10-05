package com.secondskin.babbywear.repository;

import com.secondskin.babbywear.model.Coupon;
import com.secondskin.babbywear.model.UsedCoupons;
import com.secondskin.babbywear.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UsedCouponRepository extends JpaRepository<UsedCoupons,Long> {

    boolean existsByUserInfoAndCoupon(UserInfo userInfo, Coupon coupon);



}
