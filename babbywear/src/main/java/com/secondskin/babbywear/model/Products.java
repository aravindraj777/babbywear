package com.secondskin.babbywear.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Table
@Entity
public class Products {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String productName;
    private float price;
    private String description;
    private boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;


    @OneToMany(mappedBy = "products",cascade = CascadeType.ALL)
    private List<Images> images;


    @OneToMany(mappedBy = "products",cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private List<Variant> variant = new ArrayList<>();
}
