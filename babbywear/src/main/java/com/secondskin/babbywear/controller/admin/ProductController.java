package com.secondskin.babbywear.controller.admin;



import com.secondskin.babbywear.model.Category;
import com.secondskin.babbywear.model.Images;
import com.secondskin.babbywear.model.Products;

import com.secondskin.babbywear.repository.ProductRepository;
import com.secondskin.babbywear.service.category.CategoryService;
import com.secondskin.babbywear.service.image.ImageService;
import com.secondskin.babbywear.service.imageCrop.ImageCropService;
import com.secondskin.babbywear.service.product.ProductService;
import com.secondskin.babbywear.service.review.ReviewService;
import com.secondskin.babbywear.service.user.UserService;
import net.coobird.thumbnailator.Thumbnails;

import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequestMapping("/product")
@Controller
public class ProductController {


    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductService productService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ImageCropService imageCropService;

    @Autowired
    UserService userService;

    @Autowired
    ReviewService reviewService;


    @Autowired
    ImageService imageService;



 String uploadDir = "/home/ec2-user/babbywear/src/main/resources/static/productImages";



    @GetMapping("/product-page")
    public String productPanel(Model model){
        List<Products> products = productService.findAll().stream()
                .filter(products1 -> !products1.isDeleted())
                .toList();
        model.addAttribute("product",products);
        return "admin/product";
    }


    @GetMapping("/create")
    public String index(Model model) {

        List<Category> categoryList = categoryService.getAllCategories()
                        .stream()
                                .filter(category-> !category.isDeleted())
                                        .toList();
        model.addAttribute("categories", categoryList);
        model.addAttribute("product", new Products());

        return "admin/product-create";

    }


// existing
    @PostMapping("/add-product")
    public String addProducts(@Validated @ModelAttribute Products products,
                              BindingResult bindingResult,
                              @RequestParam("images")List<MultipartFile> imageFiles,
                              @RequestParam("categoryId")Long categoryId,
                              Model model)throws IOException {

        Optional<Products> addedProduct = productService.findByName(products.getProductName());

        if(addedProduct.isPresent()){
            bindingResult.rejectValue("productName","error.productName","Product Already Exists");


            model.addAttribute("categories", categoryService.getAllCategories());
            return "admin/product-create";

        }
        Optional<Category> category = categoryService.getById(categoryId);
        if(category.isPresent()) {
            products.setCategory(category.get());
        }else {
            model.addAttribute("categories", categoryService.getAllCategories());
            return "redirect:/product/create";
        }
        List<Images> imagesList = new ArrayList<>();
        for(MultipartFile imageFile:imageFiles){
            Images images = new Images();
            String imageId= imageFile.getOriginalFilename();
            Path filePath = Paths.get(uploadDir,imageId);
            Files.write(filePath,imageFile.getBytes());
            images.setImageUrl(imageId);
            images.setProducts(products);
            imagesList.add(images);
        }
        products.setImages(imagesList);
        products.setAverageRating(products.getAverageRating());
        productService.saveProduct(products);
        return "redirect:/product/product-page";
    }





    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id ){

        productService.deleteById(id);


      return "redirect:/product/product-page";

    }


    @GetMapping("/update/{id}")
    public String editProducts(@PathVariable Long id,Model model ){
        Optional<Products> products = productRepository.findById(id);
        model.addAttribute("product",products);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/update-products";
    }



    @PostMapping("/edit")
    public String editProduct(@ModelAttribute("product") Products editedProduct) {


        Optional<Products> existingProduct = productService.findById(editedProduct.getId());

        if (existingProduct.isPresent()) {
            Products productToUpdate = existingProduct.get();


            productToUpdate.setProductName(editedProduct.getProductName());
            productToUpdate.setPrice(editedProduct.getPrice());



            productToUpdate.setDescription(editedProduct.getDescription());


            productService.saveProduct(productToUpdate);
        }

        return "redirect:/product/product-page";
    }










}