package com.secondskin.babbywear.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Entity
@Table
public class Variant {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String variantName;

    private float price;

    private String description;


    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Products products;

    private int  stock;

    private Timestamp createdTime;

    private boolean is_deleted;

    @JsonProperty("variant_id")
    public Long getId(){
        return id;
    }









}
