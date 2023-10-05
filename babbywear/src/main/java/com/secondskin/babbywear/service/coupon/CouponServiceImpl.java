package com.secondskin.babbywear.service.coupon;


import com.secondskin.babbywear.model.Coupon;
import com.secondskin.babbywear.model.CouponType;
import com.secondskin.babbywear.model.UserInfo;
import com.secondskin.babbywear.repository.CouponRepository;
import com.secondskin.babbywear.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CouponServiceImpl implements CouponService{



    @Autowired
    CouponRepository couponRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public List<Coupon> getAllCoupon() {

        List <Coupon> allCoupons = couponRepository.findAll();


        return allCoupons;
    }

    @Override
    public Coupon createCoupon(Coupon coupon) {
        coupon.setStock(coupon.getStock());
        coupon.setDiscount(coupon.getDiscount());
        coupon.setCouponType(coupon.getCouponType());
        coupon.setMinimum(coupon.getMinimum());
        coupon.setMaximum(coupon.getMaximum());
        coupon.setExpirationDate(coupon.getExpirationDate());
        return couponRepository.save(coupon);
    }

    @Override
    public List<CouponType> getAllCouponTypes() {
        List<CouponType> couponTypes = couponRepository.findDistinctCouponTypeBy();
        return couponTypes;
    }

    @Override
    public Coupon getCouponByCode(String couponCode) {
        Optional<Coupon> getCouponByCode = couponRepository.findByCode(couponCode);

        if(getCouponByCode.isPresent()){
            return getCouponByCode.get();
        }
        else {
            System.out.println("not exists");
            throw new CouponNotFoundException("Coupon Not Exists");
        }
    }

    @Override
    public float calculateDiscount(float cartTotal, Coupon coupon) {

        float discountAmount = 0.0f;


        if(coupon.getCouponType() == CouponType.PERCENTAGE){

                discountAmount = (coupon.getDiscount()/100.0f) * cartTotal;
                return discountAmount;

            }

            else if(coupon.getCouponType() == CouponType.FIXED_AMOUNT){
                discountAmount = (coupon.getDiscount());
                return discountAmount;

            }

            else {
            return cartTotal;
        }
    }

    @Override
    @Transactional
    public void updateCoupon(Coupon coupon) {
        couponRepository.save(coupon);
    }

    @Override
    public List<Coupon> getAvailableCoupons(float cartTotal,Long id) {



        return null;
    }


}
