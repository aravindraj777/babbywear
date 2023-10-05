package com.secondskin.babbywear.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Getter
@Setter
@ToString
@Table(name ="user_info")
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;
    private String lastName;

    @Column(name = "userName",unique = true)
    private String userName;

    private String email;

    @Column(name = "phoneNumber")
    private Long phone;

    private String password;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "role_id")
    private Role role;

    private boolean enabled;
//    private  boolean verified =true;

    private boolean isDeleted;

    private String otp;

    private LocalDateTime otpGeneratedTime;


    @OneToMany(mappedBy = "userInfo")
    private List<Address> address = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cart_id")
    @ToString.Exclude
    private Cart cart;


    @OneToMany(mappedBy = "userInfo")
    @ToString.Exclude
    private List<Order>orders = new ArrayList<>();

    @OneToOne(mappedBy = "userInfo",cascade = CascadeType.ALL)
    @ToString.Exclude
    private Wallet wallet;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "wishlist_id")
    @ToString.Exclude
    private Wishlist wishlist;

















}
