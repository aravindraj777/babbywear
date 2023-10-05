package com.secondskin.babbywear.service.review;

import com.secondskin.babbywear.model.ProductReview;
import com.secondskin.babbywear.model.Products;
import com.secondskin.babbywear.model.UserInfo;

import java.util.List;


public interface ReviewService {

    void createReview(int rating, String review,Products products,
                      UserInfo userInfo);



   List<ProductReview> getReviewByProducts(Products products);

    void saveUserRating(UserInfo user, Products product, int rating,String review);

    List<ProductReview> findAllReview();

    List<ProductReview> findByProductId(Long id);


}
