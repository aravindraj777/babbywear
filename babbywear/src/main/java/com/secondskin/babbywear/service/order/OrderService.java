package com.secondskin.babbywear.service.order;

import com.secondskin.babbywear.model.Order;
import com.secondskin.babbywear.model.OrderItems;
import com.secondskin.babbywear.model.Status;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface OrderService {


    Order createOrder(Order order);


    List<Order> getAllOrders();

    Order getOrderById(Long id);

    List<OrderItems> getOrderItemsByOrderId(Long id);

    void updateOrderStatus(Long orderId, Status orderStatus);

    Page<Order> findByStatus(String selectedStatus, Pageable pageable);

    Page<Order> findAll(Pageable pageable);

    Optional<Order> findOrderById(Long id);

    public Status getOrderStatusById(Long id);


    void updateOrder(Order order);
}
