package com.secondskin.babbywear.model;


import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Table
@Entity
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name ="coupon_code")
    private String code;

    private int stock;

    private float discount;
    private  float maximum;
    private float minimum;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime expirationDate;

    @Enumerated(EnumType.STRING)
    private CouponType couponType;

    private boolean isEnabled;





}
