package com.secondskin.babbywear.model;

import lombok.*;

import javax.persistence.*;
import java.sql.Blob;
import java.util.List;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "category")
public class Category  {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "category_id")
    private Long id;

    private String categoryName;


    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL,orphanRemoval = true)
    @ToString.Exclude
    private List<Products> product;

    private boolean isDeleted;








}
