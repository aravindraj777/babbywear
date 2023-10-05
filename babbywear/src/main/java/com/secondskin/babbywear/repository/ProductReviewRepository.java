package com.secondskin.babbywear.repository;

import com.secondskin.babbywear.model.ProductReview;
import com.secondskin.babbywear.model.Products;
import com.secondskin.babbywear.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReview,Long> {

    List<ProductReview> findByProducts(Products products);


    long countByProducts(Products product);

    List<ProductReview> findByUserInfoAndProducts(UserInfo user, Products product);

    List<ProductReview> findByProductsId(Long productId);
}
