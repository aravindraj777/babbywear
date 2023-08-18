package com.secondskin.babbywear.repository;

import com.secondskin.babbywear.model.Products;
import com.secondskin.babbywear.model.Variant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VariantRepository extends JpaRepository<Variant,Long> {

    List<Variant> findByProducts(Products products);

    Optional<Variant> findById(Long id);



}
