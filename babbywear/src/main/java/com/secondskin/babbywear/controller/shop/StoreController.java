package com.secondskin.babbywear.controller.shop;


import com.secondskin.babbywear.model.Products;
import com.secondskin.babbywear.model.Variant;
import com.secondskin.babbywear.repository.VariantRepository;
import com.secondskin.babbywear.service.category.CategoryService;
import com.secondskin.babbywear.service.product.ProductService;
import com.secondskin.babbywear.service.variant.VariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
//@RequestMapping("/")
public class StoreController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    ProductService productService;

    @Autowired
    VariantRepository variantRepository;

    @Autowired
    VariantService variantService;


//    @GetMapping(value = {"/","/index"})
//    public String homePage(){
//        return "index";
//    }

    @GetMapping("/")
    public String shop(Model model){
        List<Products> product = productService.findAll()
                .stream().filter(products -> !products.isDeleted())
                .toList();
        model.addAttribute("products",product);

        return "index";
    }


    @GetMapping("/product-gallery")

    public String productGallery(Model model){
       List<Products> product = productService.findAll()
               .stream().filter(products -> !products.isDeleted())
               .toList();
       model.addAttribute("products",product);
        return "product-gallery";
    }

//    @GetMapping("/single-product/{id}")
//    public String getSingleProduct(@PathVariable Long id, Model model) {
//        Optional<Products> optionalProduct =productService.findById(id);
////        model.addAttribute("product",products);
////        System.out.println("skghlgs");
////          return "single-product";
//
//            if(optionalProduct.isPresent()){
//                Products products = optionalProduct.get();
//                model.addAttribute("product",products);
//                System.out.println("sjbgjka");
//                return "single-product";
//            }
//            else {
//                return "product-not-found";
//            }
//    }

    @GetMapping("/single-product/{id}")
    public String getSingleProduct(@PathVariable Long id,Model model){

        Optional<Products> optionalProducts = productService.findById(id);

        if(optionalProducts.isPresent()){
            Products product = optionalProducts.get();
            List<Variant> variants = variantService.findByProduct(product);
            model.addAttribute("product",product);
            model.addAttribute("variants",variants);
            return "single-product";
        }
        else {
            return "product-not-found";
        }

    }


// rest-method to show variant price .
    @GetMapping("/get-variant/{id}")
    @ResponseBody
    public ResponseEntity<Variant> getVariant(@PathVariable String id){
       try {
           System.out.println(id+"id");

           Long variantId = Long.parseLong(id);
           Optional<Variant> variant = variantService.findById(variantId);
           return variant.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

       }
       catch (NumberFormatException e){
           return ResponseEntity.badRequest().build();
       }
    }





    

}
