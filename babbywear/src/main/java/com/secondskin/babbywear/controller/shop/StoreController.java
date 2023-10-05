package com.secondskin.babbywear.controller.shop;


import com.secondskin.babbywear.model.*;
import com.secondskin.babbywear.repository.VariantRepository;
import com.secondskin.babbywear.service.category.CategoryService;
import com.secondskin.babbywear.service.product.ProductService;
import com.secondskin.babbywear.service.review.ReviewService;
import com.secondskin.babbywear.service.user.UserService;
import com.secondskin.babbywear.service.variant.VariantService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import java.util.List;

import java.util.Optional;
import java.util.stream.Collectors;


@Controller

public class StoreController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    ProductService productService;

    @Autowired
    VariantRepository variantRepository;

    @Autowired
    VariantService variantService;

    @Autowired
    UserService userService;

    @Autowired
    ReviewService reviewService;





    @GetMapping("/")

    public String shop(Model model){
        List<Products> product = productService.findAll()
                .stream().filter(products -> !products.isDeleted())
                .toList();

        List<Products> mensProducts = productService.findAll()
                        .stream().filter(products -> "Men" .equals(products.getCategory().getCategoryName()))
                        .limit(4).toList();
        model.addAttribute("products",product);
        model.addAttribute("mensProduct",mensProducts);

        return "index";
    }


    @GetMapping("/product-gallery")
    public String productGallery(Model model){
       List<Products> product = productService.findAll()
               .stream().filter(products -> !products.isDeleted())
               .toList();
       List<Category>categories = categoryService.getAllCategories();

       model.addAttribute("categories",categories);
       model.addAttribute("products",product);
        return "product-gallery";
    }








    @GetMapping("/findProduct/{id}")
    public  String findProductByCategory(@PathVariable Long id,Model model){

        List<Category> categories = categoryService.getAllCategories();
        Optional<Category> category = categoryService.getById(id);

        if(category.isPresent()){
            Category selectedCategory = category.get();

            List<Products> products = productService.findByCategoryId(id)
                    .stream().filter(products1 -> !products1.isDeleted()).collect(Collectors.toList());

            model.addAttribute("products",products);
            model.addAttribute("categories",categories);
            return "product-gallery";
        }
       else {
           return "product-not-found";
        }
    }


    @GetMapping("/searchCategory")
    public String  searchCategory(@RequestParam String searchCategory,Model model){

        Optional<Category> categoryName = categoryService.findByName(searchCategory);
        if(categoryName.isPresent()){
            Category selectedCategory = categoryName.get();
            List<Products> products = productService.findByCategoryId(selectedCategory.getId())
                    .stream().filter(products1 -> !products1.isDeleted()).collect(Collectors.toList());

            model.addAttribute("selectedCategory",searchCategory);
            model.addAttribute("products",products);

        }
        else {
            return "product-not-found";
        }
        return "product-gallery";



    }



    @GetMapping("/searchProduct")
    public String searchProductsByName(@RequestParam String searchProduct, Model model) {

        List<Products> products = productService.findByName(searchProduct)
                .stream().filter(product -> !product.isDeleted()).collect(Collectors.toList());

        if (!products.isEmpty()) {
            model.addAttribute("searchedProduct", searchProduct);
            model.addAttribute("products", products);

            return "product-gallery";
        } else {
            return "product-gallery";
        }
    }
























    @GetMapping("/single-product/{id}")
    public String getSingleProduct(@PathVariable Long id,Model model,Principal principal){

        Optional<Products> optionalProducts = productService.findById(id);

        UserInfo user = userService.findByUsername(principal.getName());



        if(optionalProducts.isPresent()) {
            Products product = optionalProducts.get();
            List<Variant> variants = variantService.findByProduct(product);
            List<ProductReview> productReviews = reviewService.getReviewByProducts(product);

            boolean hasPurchased = productService.hasUserPurchasedProduct(user, product);
            boolean hasReviewed = productService.hasUserReviewed(user,product);

           List<ProductReview> allReview = reviewService.findByProductId(product.getId());



            model.addAttribute("allReview",allReview);
            model.addAttribute("hasPurchased", hasPurchased);
            model.addAttribute("hasReviewed",hasReviewed);
            model.addAttribute("product", product);
            model.addAttribute("variants", variants);
            model.addAttribute("review", productReviews);
            return "single-product";
        }
            else {
            return "product-not-found";
        }

    }










    @PostMapping("/review")
    @ResponseBody
    public ResponseEntity<String> createReview(@RequestParam String productId,
                                               @RequestParam int rating,
                                               @RequestParam String review,
                                               Principal principal) {

        Long id = Long.parseLong(productId);


        UserInfo user = userService.findByUsername(principal.getName());

        Products product = productService.getById(id);


        if (user != null && product != null) {


            if (rating >= 0 && rating <= 5) {
                reviewService.saveUserRating(user, product, rating, review);


                double averageRating = productService.calculateAverageRating(product);

                product.setAverageRating(averageRating);
                productService.saveProduct(product);


                return ResponseEntity.status(HttpStatus.FOUND)
                        .header("Location", "/product-gallery") // Replace with the actual URL
                        .build();

            } else {
                return ResponseEntity.badRequest().body("Rating must be between 1 and 5.");
            }
        }
        return ResponseEntity.badRequest().body("Unable to review");
    }











// rest-method to show variant price .
    @GetMapping("/get-variant/{id}")
    @ResponseBody
    public ResponseEntity<Variant> getVariant(@PathVariable String id){
       try {


           Long variantId = Long.parseLong(id);
           Optional<Variant> variant = variantService.findById(variantId);
           return variant.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

       }
       catch (NumberFormatException e){
           return ResponseEntity.badRequest().build();
       }
    }





    

}
