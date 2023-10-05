package com.secondskin.babbywear.service.sales;


import com.secondskin.babbywear.dto.ChartDTO;

import com.secondskin.babbywear.model.Order;
import com.secondskin.babbywear.model.SalesTime;
import com.secondskin.babbywear.model.Status;
import com.secondskin.babbywear.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import java.time.LocalDate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SalesServiceImpl implements SalesService{


    @Autowired
    OrderRepository orderRepository;



    @Override
    public long calculateWeeklyOrderCount() {


        LocalDate startDate = LocalDate.now().minusWeeks(1);
        LocalDate endDate = LocalDate.now();
        List<Order> allOrders = orderRepository.findOrderByOrderedDateBetween(startDate,endDate)
                .stream().filter(order -> order.getStatus()!=Status.ORDER_CANCELLED).toList();

        long weeklyOrderCount = allOrders. size();



        return weeklyOrderCount;

    }



    @Override
    public BigDecimal calculateWeeklyOrderSum() {




        LocalDate startDate = LocalDate.now().minusWeeks(1);


        LocalDate endDate = LocalDate.now();


        List<Order> ordersInOneWeek = orderRepository.findOrderByOrderedDateBetween(startDate,endDate).
                stream().filter(order -> order.getStatus()!=Status.ORDER_CANCELLED).toList();

         BigDecimal weeklyOrderSum = BigDecimal.valueOf(ordersInOneWeek.stream().mapToDouble(Order::getTotal).sum());



        return weeklyOrderSum;
    }



    @Override
    public long calculateDailyOrderCount(LocalDate date) {
        LocalDate today = LocalDate.now();

        List<Order> allOrders = orderRepository.findAll();

        long dailyOrderCount = allOrders.stream()
                .filter(order -> order.getOrderedDate().equals(date) && !order.getStatus().equals(Status.ORDER_CANCELLED))
                        .count();




        return dailyOrderCount;
    }

    @Override
    public BigDecimal calculateDailyOrderSum(LocalDate date) {
        List<Order> allOrders = orderRepository.findAll();

        BigDecimal dailyOrderSum = allOrders.stream()
                .filter(order -> order.getOrderedDate().equals(date) && !order.getStatus().equals(Status.ORDER_CANCELLED))
                .map(order -> BigDecimal.valueOf(order.getTotal()))
                .reduce(BigDecimal.ZERO,BigDecimal::add);

        return dailyOrderSum;
    }

    @Override
    public long calculateMonthlyOrderCount() {

        LocalDate startDate = LocalDate.now().minusMonths(1);
        LocalDate endDate = LocalDate.now();

        List<Order> allOrder = orderRepository.findOrderByOrderedDateBetween(startDate,endDate).
                stream().filter(order -> order.getStatus()!=Status.ORDER_CANCELLED).toList();

        long monthlyOrderCount = allOrder.size();

        return monthlyOrderCount;
    }

    @Override
    public BigDecimal calculateMonthlyOrderSum() {


        LocalDate startDate = LocalDate.now().minusMonths(1);
        LocalDate endDate = LocalDate.now();

        List<Order> allOrders = orderRepository.findAll().stream().
                                filter(order -> order.getStatus()!=Status.ORDER_CANCELLED).toList();


        BigDecimal monthlyOrderTotal = BigDecimal.valueOf(allOrders.stream().mapToDouble(Order::getTotal).sum());

        return monthlyOrderTotal;
    }

    @Override
    public long calculateYearlyOrderCount() {


        LocalDate startDate = LocalDate.now().minusYears(1);
        LocalDate endDate = LocalDate.now();

        List<Order> allOrder  = orderRepository.findAll().stream()
                .filter(order -> order.getStatus()!=Status.ORDER_CANCELLED).toList();

        long yearlyOrderCount = allOrder.size();


        return yearlyOrderCount;
    }

    @Override
    public BigDecimal calculateYearlyOrderSum() {


        LocalDate startDate = LocalDate.now().minusYears(1);
        LocalDate endDate = LocalDate.now();

        List <Order> allOrder = orderRepository.findAll().stream().filter(order -> order.getStatus()!=Status.ORDER_CANCELLED)
                .toList();

        BigDecimal yearlyOrderTotal  = BigDecimal.valueOf(allOrder.stream().mapToDouble(Order::getTotal).sum());

        return yearlyOrderTotal;
    }

    @Override
    public long countDeliveredOrders() {
        List<Order> deliveredOrders = orderRepository.findByStatus(Status.ORDERED_DELIVERED);
        return  deliveredOrders.stream().count();
    }

    @Override
    public List<Order> getOrderBySelectedDate(LocalDate startDate, LocalDate endDate) {
        return orderRepository.findOrderByOrderedDateBetween(startDate,endDate);
    }

    @Override
    public float calculateTotalSales(List<Order> orders) {
        float total = 0;
        for(Order order : orders){
            total +=order.getTotal();
        }

        return total;
    }

    @Override
    public List<ChartDTO> calculateWeeklyOrderCountAndDate() {



            List<ChartDTO> chartDataList = new ArrayList<>();

            LocalDate endDate = LocalDate.now();
            LocalDate startDate = endDate.minusWeeks(1);

            while (!startDate.isAfter(endDate)) {
                long orderCount = orderRepository.findOrderByOrderedDateBetween(startDate, startDate.plusDays(1).minusDays(1)).size();

                ChartDTO dto = new ChartDTO();
                dto.setDate(LocalDate.parse(startDate.toString())); // Format the date as needed
                dto.setOrderCount(orderCount);

                chartDataList.add(dto);

                startDate = startDate.plusDays(1);
            }

            return chartDataList;
        }


    @Override
    public List<ChartDTO> calculateMonthlyOrderCountAndDate() {

        List<ChartDTO> chartDataList = new ArrayList<>();

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusMonths(12).plusDays(1); // One year ago from today + 1 day

        List<Order> orders = orderRepository.findOrderByOrderedDateBetween(startDate, endDate);

        // Group orders by year and month
        Map<Integer, Map<Integer, List<Order>>> groupedOrders = orders.stream()
                .collect(Collectors.groupingBy(
                        order -> order.getOrderedDate().getYear(),
                        Collectors.groupingBy(order -> order.getOrderedDate().getMonthValue())
                ));

        // Iterate over the grouped data and calculate monthly order counts
        for (Map.Entry<Integer, Map<Integer, List<Order>>> yearEntry : groupedOrders.entrySet()) {
            int year = yearEntry.getKey();
            Map<Integer, List<Order>> monthEntries = yearEntry.getValue();

            for (Map.Entry<Integer, List<Order>> monthEntry : monthEntries.entrySet()) {
                int month = monthEntry.getKey();
                List<Order> monthlyOrders = monthEntry.getValue();

                long orderCount = monthlyOrders.size();

                ChartDTO dto = new ChartDTO();
                dto.setDate(LocalDate.of(year, month, 1)); // Set the date as the first day of the month
                dto.setOrderCount(orderCount);

                chartDataList.add(dto);
            }
        }

        return chartDataList;
    }



    @Override
    public List<ChartDTO> calculateYearlyOrderCountAndDate() {



        List<ChartDTO> chartDataList = new ArrayList<>();

        LocalDate startDate = LocalDate.now().minusYears(1);
        LocalDate endDate = LocalDate.now();

        while (!startDate.isAfter(endDate)) {
            long orderCount = orderRepository.findOrderByOrderedDateBetween(startDate, startDate.plusDays(1).minusDays(1)).size();

            ChartDTO dto = new ChartDTO();
            dto.setDate(LocalDate.parse(startDate.toString())); // Format the date as needed
            dto.setOrderCount(orderCount);

            chartDataList.add(dto);

            startDate = startDate.plusDays(1);
        }

        return chartDataList;
    }

    @Override
    public List<Order> findByTimePeriod(SalesTime salesTime) {


        LocalDate startDate;
        LocalDate endDate = LocalDate.now();

        switch (salesTime){
            case MONTHLY -> {
                startDate = endDate.minusMonths(1);
                break;
            }
            case WEEKLY -> {
                startDate = endDate.minusDays(6);
                break;
            }
            case DAILY -> {
                startDate = endDate;
                break;
            }
            default -> {
                throw new IllegalArgumentException("timePeriod not available"+salesTime);
            }
        }
        return orderRepository.findOrderByOrderedDateBetween(startDate,endDate);

    }



}





