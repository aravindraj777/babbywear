package com.secondskin.babbywear.repository;

import com.secondskin.babbywear.model.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Products, Long> {

    Optional<Products> findByProductName(String name);

    List<Products> findByCategoryId(Long categoryId);

    Products getById(Long id);

    Page<Products> findByIsDeletedFalse(Pageable pageable);



}
