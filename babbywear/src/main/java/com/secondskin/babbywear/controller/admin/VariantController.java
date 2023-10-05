package com.secondskin.babbywear.controller.admin;

import com.secondskin.babbywear.model.Products;
import com.secondskin.babbywear.model.Variant;
import com.secondskin.babbywear.service.product.ProductService;
import com.secondskin.babbywear.service.variant.VariantService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/variant")
public class VariantController {


    @Autowired
    VariantService variantService;

    @Autowired
    ProductService productService;

    @GetMapping("/variant-list")
    public String  variantList(Model model){
        List<Variant> variants  = variantService.getAllVariantsWithProducts();

        model.addAttribute("variants",variants);
        return "admin/variant";
    }


    @GetMapping("/create")
    public String showVariantForm( Model model) {
        List<Products> products = productService.findAll();
        model.addAttribute("products", products);
//        model.addAttribute("productId", productId);
        model.addAttribute("variant", new Variant());
        return "admin/create-variant";
    }

    @PostMapping("/create")
    public String createVariant(@ModelAttribute Variant variant){
        Long productId = variant.getProducts().getId();
        variantService.createVariantWithProduct(variant,productId);
        return "redirect:/variant/variant-list";
    }


    @GetMapping("/low-stock")
    public String showLowStock(Model model){
        List<Variant> lowStockVariants = variantService.getVariantWithLowStock();
        model.addAttribute("lowStock",lowStockVariants);
        return "admin/low-stock-list";
    }

    @GetMapping("/edit-variant/{id}")
    public String editVariant(@PathVariable Long id,Model model){


            Optional<Variant> optionalVariant = variantService.findById(id);
            if (optionalVariant.isPresent()) {
                Variant variant = optionalVariant.get();
                model.addAttribute("product", variant.getProducts());
                model.addAttribute("variants", variant);
                return "admin/edit-variant";
            } else {
                return "product-not-found";
            }


    }

    @PostMapping("/updateVariant")
    public String updateVariant(@ModelAttribute("variants") Variant variant){
        variantService.updateVariant(variant.getId(),variant);
        return "redirect:/variant/variant-list";
    }



}
