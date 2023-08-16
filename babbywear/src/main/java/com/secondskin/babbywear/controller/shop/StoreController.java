package com.secondskin.babbywear.controller.shop;


import com.secondskin.babbywear.model.Products;
import com.secondskin.babbywear.service.category.CategoryService;
import com.secondskin.babbywear.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.sound.midi.MidiChannel;
import java.util.List;
import java.util.Optional;

@Controller
//@RequestMapping("/")
public class StoreController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    ProductService productService;


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

    @GetMapping("/single-product/{id}")
    public String getSingleProduct(@PathVariable Long id, Model model) {
        Optional<Products> optionalProduct =productService.findById(id);
//        model.addAttribute("product",products);
//        System.out.println("skghlgs");
//          return "single-product";

            if(optionalProduct.isPresent()){
                Products products = optionalProduct.get();
                model.addAttribute("product",products);
                System.out.println("sjbgjka");
                return "single-product";
            }
            else {
                return "product-not-found";
            }
    }





    

}
