package com.secondskin.babbywear.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "address")
@Entity
public class Address {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Address_id")
    private Long id;

    private String  flat;
    private String area;
    private String town;
    private String city;
    private String state;
    private String pin;
    private String landMark;
    private boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserInfo userInfo;


}
