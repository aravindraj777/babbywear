package com.secondskin.babbywear.service.category;

import com.secondskin.babbywear.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {



    void addCategory(Category category);

    Category getByCategoryName(String name);

    List<Category> getAllCategories();

    Optional<Category> getById(Long id);

     void deleteById(Long id);

     void updateCategory(Category category);












}
