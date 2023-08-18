package com.secondskin.babbywear.service.variant;

import com.secondskin.babbywear.model.Products;
import com.secondskin.babbywear.model.Variant;
import com.secondskin.babbywear.repository.VariantRepository;
import com.secondskin.babbywear.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class VariantServiceImpl implements VariantService{

    @Autowired
    VariantRepository variantRepository;

    @Autowired
    ProductService productService;



    @Override
    public Variant createVariantWithProduct(Variant variant, Long productId) {
        Optional<Products> products = productService.findById(productId);

        if(products.isPresent()){
            Products product = products.get();
            variant.setProducts(product);
            return variantRepository.save(variant);

        }
        else {
            throw new ProductNotFoundException("Product with ID"+productId+"Not found");
        }
    }

    @Override
    public List<Variant> getAllVariantsWithProducts() {
        return variantRepository.findAll();
    }

    @Override
    public List<Variant> findByProduct(Products products) {
        return variantRepository.findByProducts(products);
    }

    @Override
    public Optional<Variant> findById(Long id) {
        return variantRepository.findById(id);
    }


}
