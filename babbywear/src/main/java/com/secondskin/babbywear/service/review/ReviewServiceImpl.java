package com.secondskin.babbywear.service.review;

import com.secondskin.babbywear.model.ProductReview;
import com.secondskin.babbywear.model.Products;
import com.secondskin.babbywear.model.UserInfo;
import com.secondskin.babbywear.repository.ProductReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ReviewServiceImpl implements ReviewService {


    @Autowired
    ProductReviewRepository productReviewRepository;


    @Override
    public void createReview(int rating, String review, Products products, UserInfo userInfo) {
        createReviewHelper(rating, review, products, userInfo);

    }

    private void createReviewHelper(int rating, String review, Products products, UserInfo userInfo) {
        ProductReview productReview = new ProductReview();
        productReview.setReview(review);
        productReview.setProducts(products);
        productReview.setRating(rating);
        productReview.setUserInfo(userInfo);
        productReviewRepository.save(productReview);
    }

    @Override
    public List<ProductReview> getReviewByProducts(Products products) {
        return productReviewRepository.findByProducts(products);
    }

    @Override
    public void saveUserRating(UserInfo user, Products product, int rating, String review) {

        ProductReview productReview = ProductReview.
                builder().
                userInfo(user).
                products(product).
                rating(rating).
                review(review).
                build();


        productReviewRepository.save(productReview);


    }

    @Override
    public List<ProductReview> findAllReview() {
        return productReviewRepository.findAll();
    }

    @Override
    public List<ProductReview> findByProductId(Long id) {

        return productReviewRepository.findByProductsId(id);
    }



}




