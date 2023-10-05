package com.secondskin.babbywear.controller.admin;



import com.secondskin.babbywear.model.Category;
import com.secondskin.babbywear.repository.CategoryRepository;

import com.secondskin.babbywear.service.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/category")
public class CategoryController {


    @Autowired
    CategoryService categoryService;

    @Autowired
    CategoryRepository categoryRepository;





    @GetMapping("/create")
    public String createCategory(Model model){

        List<Category> categoryList = categoryService.getAllCategories()
                        .stream()
                                .filter(category -> !category.isDeleted())
                                        .toList();
        model.addAttribute("categories", categoryList);

        return "admin/categorylist";
    }





//Save category main....

    @PostMapping("/save")
    public String saveCategory(@ModelAttribute Category category, Model model, BindingResult result) {
        String name = category.getCategoryName();
        Category existingCategory = categoryService.getByCategoryName(name);

        if(existingCategory!=null && existingCategory.isDeleted()){
            existingCategory.setDeleted(false);
            categoryService.updateCategory(existingCategory);
        } else if (existingCategory!=null && !existingCategory.isDeleted()) {
            result.rejectValue("categoryName","error.categoryName","Category Already Exists");
            return "admin/categorylist";
            
        }
        else {
            category.setDeleted(false);
            categoryService.addCategory(category);
        }



        return "redirect:/category/create"; // Corrected the return view name
    }





    @GetMapping("/delete/{id}")
    public String deleteCategories(@PathVariable Long id){
       return categoryService.getById(id)
               .map(category -> {
                   categoryService.deleteById(id);
                   return "redirect:/category/create";
               })
               .orElse("redirect:/category/create");
    }

    @GetMapping("/update/{id}")
    public String updateCategories(@PathVariable Long id,Model model){
       Optional<Category> category =  categoryService.getById(id);
       model.addAttribute("updateCategory",category);
       return "admin/update-category";

    }



   @PostMapping("/edit")
    public String updateCategory(@ModelAttribute("updateCategory") Category editedCategory){

        Optional<Category> existingCategory = categoryService.getById(editedCategory.getId());

        if(existingCategory.isPresent()){
            Category categoryToUpdate = existingCategory.get();

            categoryToUpdate.setCategoryName(editedCategory.getCategoryName());
            categoryService.addCategory(categoryToUpdate);
        }
        return "redirect:/category/create";
    }



}
