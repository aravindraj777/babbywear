package com.secondskin.babbywear.model;


import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Entity
@Table(name = "wishlist")
public class Wishlist {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @OneToOne
    @JoinColumn(name = "user_id")
    private UserInfo userInfo;

    @OneToMany(mappedBy = "wishlist",cascade = CascadeType.ALL,orphanRemoval = true)
    @ToString.Exclude
    private List<WishListItem> wishListItems = new ArrayList<>();

}
