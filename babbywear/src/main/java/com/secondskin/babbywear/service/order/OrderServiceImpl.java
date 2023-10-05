package com.secondskin.babbywear.service.order;

import com.razorpay.RazorpayClient;
import com.secondskin.babbywear.model.*;
import com.secondskin.babbywear.repository.OrderItemRepository;
import com.secondskin.babbywear.repository.OrderRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements  OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderItemRepository orderItemRepository;


    private static String KEY = "rzp_test_BRpuhgTgnU69VA";
    private static String KEY_SECRET = "ldo9ON2Qychqnqyg4GtedHGz";

    private static String CURRENCY = "INR";


    @Override
    public Order createOrder(Order order) {

        return orderRepository.save(order);
    }


    public void createTransaction(Double amount) {
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("amount", (amount * 100));
            jsonObject.put("currency", CURRENCY);
            RazorpayClient razorpayClient = new RazorpayClient(KEY, KEY_SECRET);
            com.razorpay.Order order = razorpayClient.orders.create(jsonObject);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }


    @Override
    public List<Order> getAllOrders() {

        return orderRepository.findAll();
    }

    @Override
    public Order getOrderById(Long id) {
        Order order = orderRepository.findById(id).orElse(null);
        System.out.println(order);
        return order;
    }

    @Override
    public List<OrderItems> getOrderItemsByOrderId(Long id) {

        return orderItemRepository.findByOrderId(id);
    }

    @Override
    public void updateOrderStatus(Long orderId, Status orderStatus) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        LocalDate currentDate = LocalDate.now();

        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();

            switch (orderStatus) {
                case ORDER_PENDING -> order.setStatus(Status.ORDER_PENDING);
                case ORDERED_DELIVERED -> {
                    order.setDeliveryDate(currentDate);
                    order.setStatus(Status.ORDERED_DELIVERED);
                }
                case SHIPPING -> {
                    order.setShippedDate(currentDate);
                    order.setStatus(Status.SHIPPING);
                }
                case ORDER_RETURNED -> {
                    order.setReturnDate(currentDate);
                    order.setStatus(Status.ORDER_RETURNED);
                }
                default -> {
                    order.setStatus(orderStatus);
                }

            }
            orderRepository.save(order);
        } else {
            throw new OrderNotFoundException("Order Not Found" + orderId);
        }
    }

    @Override
    public Page<Order> findByStatus(String selectedStatus, Pageable pageable) {
        return orderRepository.findByStatus(selectedStatus, pageable);
    }

    @Override
    public Page<Order> findAll(Pageable pageable) {

        return orderRepository.findAll(pageable);
    }

    @Override
    public Optional<Order> findOrderById(Long id) {

        return orderRepository.findById(id);


    }

    @Override
    public Status getOrderStatusById(Long id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            return order.getStatus();
        } else {
            return null;
        }

    }

    @Override
    public void updateOrder(Order order) {
        orderRepository.save(order);
    }
}
