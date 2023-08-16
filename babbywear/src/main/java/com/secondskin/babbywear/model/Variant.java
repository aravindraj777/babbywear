package com.secondskin.babbywear.model;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table
public class Variant {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String variantName;

    private float price;

    private String description;


    @ManyToOne
    @JoinColumn(name = "product_id")
    private Products products;

    private int  stock;

    private Timestamp createdTime;

    private boolean is_deleted;





}
