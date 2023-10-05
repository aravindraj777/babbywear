package com.secondskin.babbywear.service.variant;

import com.secondskin.babbywear.model.Products;
import com.secondskin.babbywear.model.Variant;

import java.util.List;
import java.util.Optional;

public interface VariantService {

    Variant createVariantWithProduct(Variant variant,Long productId);

    List<Variant> getAllVariantsWithProducts();

    List<Variant>findByProduct(Products products);

    Optional<Variant> findById(Long id);

    void decreaseQuantity(Variant variant);

    void checkStockAndNotifyAdmin();

    List<Variant> getVariantWithLowStock();


    void updateVariant(Long id, Variant variant);
}
