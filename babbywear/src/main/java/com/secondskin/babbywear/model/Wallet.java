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
@Table
public class Wallet {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "balance")
    private float balance = 0.0f;

    @JoinColumn(name = "user_id")
    @OneToOne
    private UserInfo userInfo;
}
