package com.secondskin.babbywear.service.stock;

import com.secondskin.babbywear.model.Order;

public interface StockService {


    void updateStockForOrderCancellation(Order order);
}
