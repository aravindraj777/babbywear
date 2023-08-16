package com.secondskin.babbywear.service.product;

import com.secondskin.babbywear.model.Products;

import java.util.List;
import java.util.Optional;

public interface ProductService {

     Products saveProduct(Products products);

    List<Products> findAll();

    Optional<Products> findByName(String productName );

     Optional<Products>findById(Long id);

     void deleteById(Long id);

//     void updateProducts(Long id,Products products);

    void editById(Long id,Products products);


}
