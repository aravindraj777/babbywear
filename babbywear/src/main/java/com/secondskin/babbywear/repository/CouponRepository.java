package com.secondskin.babbywear.repository;

import com.secondskin.babbywear.model.Coupon;
import com.secondskin.babbywear.model.CouponType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface CouponRepository  extends JpaRepository<Coupon,Long> {

    Optional<Coupon> findByCode(String code);

    List<CouponType> findDistinctCouponTypeBy();

    String getCouponByCode(String couponCode);


}
