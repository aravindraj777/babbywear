package com.secondskin.babbywear.model;


import lombok.*;

import javax.persistence.*;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Table(name = "order_items")
@Entity
public class OrderItems {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @JoinColumn(name = "variant_id")
    @ManyToOne
    private Variant variant;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private int quantity;

    private float price;

    public  float total(){
       return this.price * this.quantity;
    }













}
