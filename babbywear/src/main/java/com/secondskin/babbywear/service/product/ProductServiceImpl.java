package com.secondskin.babbywear.service.product;

import com.secondskin.babbywear.model.Products;
import com.secondskin.babbywear.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    ProductRepository productRepository;


    @Override
    public Products saveProduct(Products products) {
        return productRepository.save(products);
    }

    @Override
    public List<Products> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Products> findByName(String productName) {
        return productRepository.findByProductName(productName);
    }

    @Override
    public Optional<Products> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        Optional<Products> products= productRepository.findById(id);
        products.ifPresent(products1 -> {
            System.out.println("irhjgpoiuehigfuhdfpioguhdpofiughpodifhgo");
            products1.setDeleted(true);
            productRepository.save(products1);
        });
    }

    @Override
    public void editById(Long id ,Products products) {
        Optional<Products> productsById = productRepository.findById(id);
        Products editProducts = productsById.get();

            editProducts.setProductName(products.getProductName());
            editProducts.setPrice(products.getPrice());
            editProducts.setCategory(products.getCategory());
            editProducts.setImages(products.getImages());
            editProducts.setDescription(products.getDescription());


            productRepository.save(editProducts);
    }


}



