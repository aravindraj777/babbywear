package com.secondskin.babbywear.repository;

import com.secondskin.babbywear.model.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Products, Long> {

    Optional<Products> findByProductName(String name);

}
