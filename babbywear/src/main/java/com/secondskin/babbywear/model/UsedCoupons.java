package com.secondskin.babbywear.model;


import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Entity
@Table(name = "used_coupons")
public class UsedCoupons {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private  UserInfo userInfo;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;
}
