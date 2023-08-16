package com.secondskin.babbywear.controller.admin;


import com.secondskin.babbywear.dto.ProductDto;
import com.secondskin.babbywear.model.Category;
import com.secondskin.babbywear.model.Images;
import com.secondskin.babbywear.model.Products;
import com.secondskin.babbywear.repository.ProductRepository;
import com.secondskin.babbywear.service.category.CategoryService;
import com.secondskin.babbywear.service.image.ImageService;
import com.secondskin.babbywear.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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
    ImageService imageService;

    public static String uploadDir = "C:\\Users\\ARAVIND\\OneDrive\\Desktop\\babbywear\\babbywear\\src\\main\\resources\\static\\productImages";



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



    @PostMapping("/add-product")
    public String addProducts(@Validated @ModelAttribute Products products,
//
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
        productService.saveProduct(products);
        return "redirect:/product/product-page";
    }



//    private String saveImageToDisk(MultipartFile imageFile,Products products)throws IOException{
//        String imageName = System.currentTimeMillis()+"/"+imageFile.getOriginalFilename();
//        String imageUrl = "/src/main/resources/static/assets/images/products/productImages/"+imageName;
//        Images images = new Images(imageUrl,products);
//        imageService.saveImages(images);
//        return imageUrl;
//
//
//    }

//    @Autowired
//    private ResourceLoader resourceLoader;
//
//    private String saveImageToDisk(MultipartFile imageFile, Products products) throws IOException {
//        String imageName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
//        String imagePath = "/src/main/resources/static/assets/images/products/productImages/" + imageName;
//
//        // Get the file resource from the provided path
//        Resource resource = resourceLoader.getResource(imagePath);
//
//        // Get the absolute file path
//        File file = resource.getFile();
//        String imageUrl = imagePath+imageName;
//
//        // Save the image to the file location
//        try (OutputStream outputStream = new FileOutputStream(file)) {
//            outputStream.write(imageFile.getBytes());
//        }
//
//        // Create the Images object and save it to the database
//        Images images = new Images(imageUrl, products);
//        imageService.saveImages(images);
//
//        return imageUrl;
//    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id ){
        System.out.println("eltjghiejhgpiuehbgi");
        productService.deleteById(id);
        System.out.println("ljehgipuehpgiuehpriugheipruh");
//      if(productService.findById(id).isPresent()){
//          productService.deleteById(id);
//          return "redirect:/product/product-page";
//      }
      return "redirect:/product/product-page";

    }


    @GetMapping("/update/{id}")
    public String editProducts(@PathVariable Long id,Model model ){
        Optional<Products> products = productRepository.findById(id);
        model.addAttribute("product",products);
        return "admin/update-products";
    }

    @PostMapping("/edit")
    public String editProduct(@ModelAttribute("product")Products products){
            productService.editById(products.getId(),products);
            return "redirect:/product/product-page";
    }



}