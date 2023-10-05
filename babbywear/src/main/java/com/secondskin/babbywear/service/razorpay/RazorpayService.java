package com.secondskin.babbywear.service.razorpay;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RazorpayService {

    @Value("${razorpay.api.key.id}")
    private String razorpayKey;

    @Value("${razorpay.api.key.secret}")
    private String razorpaySecret;

    public Order createOrder(float amount, String currency) throws RazorpayException {

        try {
            RazorpayClient razorpayClient = new RazorpayClient(razorpayKey, razorpaySecret);
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amount * 100);
            orderRequest.put("currency", currency);
            Order order = razorpayClient.orders.create(orderRequest);
            return order;
        } catch (Exception e) {
            throw new RazorpayException("Failed to create Order" + e.getMessage());
        }

    }
}
