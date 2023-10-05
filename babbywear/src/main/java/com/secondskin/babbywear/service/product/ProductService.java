package com.secondskin.babbywear.service.product;

import com.secondskin.babbywear.model.Products;
import com.secondskin.babbywear.model.UserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductService {

     Products saveProduct(Products products);

    List<Products> findAll();

    Optional<Products> findByName(String productName );

     Optional<Products>findById(Long id);

     Products getById(Long id);

     void deleteById(Long id);



    void editById(Long id,Products products);

    List<Products> findByCategoryId(Long categoryId);

    int calculateTotalRating(Products product);

    double calculateAverageRating(Products product);

    boolean hasUserPurchasedProduct(UserInfo user,Products products);


    boolean hasUserReviewed(UserInfo user, Products product);

    Page<Products> findPaginatedProducts(Pageable pageable);
}
