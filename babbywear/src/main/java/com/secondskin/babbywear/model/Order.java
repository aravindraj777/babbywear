package com.secondskin.babbywear.model;


import lombok.*;


import javax.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Table(name = "Orders")
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    @ToString.Exclude
    private UserInfo userInfo;


    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
    @ToString.Exclude
    List<OrderItems> orderItems = new ArrayList<>();

    @JoinColumn(name = "address_id")
    @ManyToOne
    private Address address;

    @Enumerated(EnumType.STRING)
    Payment payment;


    @Enumerated(EnumType.STRING)
    Status status;

    private float total;


    @Column(updatable = false)
    private LocalDate orderedDate;

    private LocalDate deliveryDate;

    private LocalDate returnDate;
    private LocalDate shippedDate;
    private LocalDate cancelledDate;



    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMBERS = "123456789";




    @Column(unique = true)
    private String orderNumber;
    @PrePersist
    public void generateRandomNumber(){
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for(int i = 0;i < 10;i++){
            if(i%2==0){
                char randomChar = CHARACTERS.charAt(random.nextInt(CHARACTERS.length()));
                sb.append(randomChar);
            }
            else{
                char randomNumber = NUMBERS.charAt(random.nextInt(NUMBERS.length()));
                sb.append(randomNumber);
            }
        }
        orderNumber = sb.toString();

   }











}
