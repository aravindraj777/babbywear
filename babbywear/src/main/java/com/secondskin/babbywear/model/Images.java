package com.secondskin.babbywear.model;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table
@Entity
//@Data
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
