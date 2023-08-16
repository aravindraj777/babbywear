package com.secondskin.babbywear.repository;

import com.secondskin.babbywear.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {

    Category getByCategoryName(String categoryName);
    Category getById(Long categoryId);




}
