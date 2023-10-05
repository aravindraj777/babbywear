package com.secondskin.babbywear.service.sales;


import com.secondskin.babbywear.dto.ChartDTO;
import com.secondskin.babbywear.model.Order;
import com.secondskin.babbywear.model.SalesTime;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface SalesService {




    BigDecimal calculateWeeklyOrderSum();
    long calculateWeeklyOrderCount();

    long calculateDailyOrderCount(LocalDate date);

    BigDecimal calculateDailyOrderSum(LocalDate date);

    long calculateMonthlyOrderCount();

    BigDecimal calculateMonthlyOrderSum();

    long calculateYearlyOrderCount();

    BigDecimal calculateYearlyOrderSum();

    long countDeliveredOrders();

    List<Order> getOrderBySelectedDate(LocalDate startDate,LocalDate endDate );

    float calculateTotalSales(List<Order> orders);


     List<ChartDTO> calculateWeeklyOrderCountAndDate() ;

    List<ChartDTO> calculateMonthlyOrderCountAndDate();

    List<ChartDTO> calculateYearlyOrderCountAndDate();

    List<Order>findByTimePeriod(SalesTime salesTime);














}
