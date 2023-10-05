package com.secondskin.babbywear.model;

import lombok.*;


import javax.persistence.*;



@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table
@Entity
@Getter
@Setter
public class Images {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Products products;









}
