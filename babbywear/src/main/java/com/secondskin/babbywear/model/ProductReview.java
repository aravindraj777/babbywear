package com.secondskin.babbywear.model;


import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Table
@Entity
public class ProductReview {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int rating ;

    private  String review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id",nullable = false)
    private Products products;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private UserInfo userInfo;


}
