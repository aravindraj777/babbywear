package com.secondskin.babbywear.service.stock;

import com.secondskin.babbywear.model.Order;
import com.secondskin.babbywear.model.OrderItems;
import com.secondskin.babbywear.model.Variant;
import com.secondskin.babbywear.repository.VariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StockServiceImpl implements StockService {


    @Autowired
    VariantRepository variantRepository;


    @Override
    @Transactional
    public void updateStockForOrderCancellation(Order order) {

        List<OrderItems> orderItems = order.getOrderItems();

        Map<Variant,Integer> stockUpdatesOfVariant = new HashMap<>();

        for(OrderItems orderItem: orderItems){
            Variant variant = orderItem.getVariant();
            int orderedQuantity = orderItem.getQuantity();



            stockUpdatesOfVariant.put(variant,stockUpdatesOfVariant.getOrDefault(variant,0)+orderedQuantity);
        }

        for(Map.Entry<Variant,Integer> entry : stockUpdatesOfVariant.entrySet()){
            Variant variant = entry.getKey();
            int stockUpdate = entry.getValue();

            int currentStock = variant.getStock();
            variant.setStock(currentStock+stockUpdate);


        }
        variantRepository.saveAll(stockUpdatesOfVariant.keySet());


    }
}
