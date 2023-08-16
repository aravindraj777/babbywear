package com.secondskin.babbywear.service.category;

import com.secondskin.babbywear.model.Category;
import com.secondskin.babbywear.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    CategoryRepository categoryRepository;
    @Override
    public void addCategory(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public Category getByCategoryName(String name) {
        return categoryRepository.getByCategoryName(name);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> getById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
      Optional<Category> category = categoryRepository.findById(id);
      category.ifPresent(category1 -> {
          category1.setDeleted(true);
          categoryRepository.save(category1);
      });

    }

    @Override
    public void updateCategory(Category category) {
        categoryRepository.save(category);
    }


//    public String getImageUrlForCategory(Category category) {
//        Category fetchedCategory = categoryRepository.findById(category.getId())
//                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
//
//        return fetchedCategory.getImageUrl();
//    }





}
