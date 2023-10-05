package com.secondskin.babbywear.service.product;

import com.secondskin.babbywear.model.OrderItems;
import com.secondskin.babbywear.model.ProductReview;
import com.secondskin.babbywear.model.Products;
import com.secondskin.babbywear.model.UserInfo;
import com.secondskin.babbywear.repository.OrderItemRepository;
import com.secondskin.babbywear.repository.ProductRepository;
import com.secondskin.babbywear.repository.ProductReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductReviewRepository productReviewRepository;

    @Autowired
    OrderItemRepository orderItemRepository;


    @Override
    public Products saveProduct(Products products) {
        return productRepository.save(products);
    }

    @Override
    public List<Products> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Products> findByName(String productName) {
        return productRepository.findByProductName(productName);
    }

    @Override
    public Optional<Products> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Products getById(Long id) {
        return productRepository.getById(id);
    }

    @Override
    public void deleteById(Long id) {
        Optional<Products> products= productRepository.findById(id);
        products.ifPresent(products1 -> {
            System.out.println("irhjgpoiuehigfuhdfpioguhdpofiughpodifhgo");
            products1.setDeleted(true);
            productRepository.save(products1);
        });
    }

    @Override
    public void editById(Long id ,Products products) {
        Optional<Products> productsById = productRepository.findById(id);
        Products editProducts = productsById.get();

            editProducts.setProductName(products.getProductName());
            editProducts.setPrice(products.getPrice());
            editProducts.setCategory(products.getCategory());
//            editProducts.setImages(products.getImages());
            editProducts.setDescription(products.getDescription());


            productRepository.save(editProducts);
    }

    @Override
    public List<Products> findByCategoryId(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    @Override
    public int calculateTotalRating(Products product) {
        long reviewCount =  productReviewRepository.countByProducts(product);


        return (int) reviewCount * 5;
    }

    @Override
    public double calculateAverageRating(Products product) {



         int totalRating = calculateTotalRating(product);
        List<ProductReview> productReviews =  productReviewRepository.findByProducts(product);

        int totalRatingOfProduct  = 0;

        for(ProductReview productReview:productReviews){
            totalRatingOfProduct += productReview.getRating();
        }
        System.out.println(totalRating);
        System.out.println(totalRatingOfProduct);

        double avgRating  = ((double) totalRatingOfProduct / totalRating) * 100.00;
        System.out.println(avgRating);

        return avgRating;
    }

    @Override
    public boolean hasUserPurchasedProduct(UserInfo user, Products products) {

        List<OrderItems> orderItems = orderItemRepository.findByOrderUserInfoAndVariant_Products(user,products);

        return !orderItems.isEmpty();
    }

    @Override
    public boolean hasUserReviewed(UserInfo user, Products product) {

        List<ProductReview> userReview = productReviewRepository.findByUserInfoAndProducts(user,product);
        return !userReview.isEmpty();
    }

    @Override
    public Page<Products> findPaginatedProducts(Pageable pageable) {
        return productRepository.findByIsDeletedFalse(pageable);
    }


}



