package com.secondskin.babbywear.repository;

import com.secondskin.babbywear.model.OrderItems;
import com.secondskin.babbywear.model.Products;
import com.secondskin.babbywear.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItems,Long> {

    List<OrderItems> findByOrderId(Long orderId);

    List<OrderItems> findByOrderUserInfoAndVariant_Products(UserInfo userInfo, Products products);




}
