package com.secondskin.babbywear.controller.admin;


import com.secondskin.babbywear.model.*;
import com.secondskin.babbywear.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/orders")
public class AdminOrderController {

    @Autowired
    OrderService orderService;



//
//    @GetMapping("/user-orders")
//    public String showOrders(Model model){
//
//        List<Order> orderList = orderService.getAllOrders();
//        model.addAttribute("orders",orderList);
//
//        return "admin/user-orders";
//    }


    @GetMapping("/user-orders")
    public String showOrder(@RequestParam(defaultValue = "0")int pageNo,
                            @RequestParam(defaultValue = "10")int pageSize,
                            @RequestParam(defaultValue = "id")String sortField,
                            @RequestParam(defaultValue = "asc")String sortOrder,
                            @RequestParam(required = false) String selectedStatus,
                            Model model){


        if (pageNo < 0) {
            pageNo = 0;
        }
        Sort sort = Sort.by(Sort.Direction.fromString(sortOrder),sortField);

        PageRequest pageRequest = PageRequest.of(pageNo,pageSize,sort);
        Page<Order> page;


        if(selectedStatus !=null && !selectedStatus.isEmpty()){
            page = orderService.findByStatus(selectedStatus,pageRequest);

        }
        else {
            page = orderService.findAll(pageRequest);
        }

        List<Status> allStatus = getStatus();

        model.addAttribute("orders",page);
        model.addAttribute("currentPage",pageNo);
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("sortField",sortField);
        model.addAttribute("sortOrder",sortOrder);
        model.addAttribute("status",allStatus);

        return "admin/user-orders";
    }




    @GetMapping("/view-order/{id}")
    public String viewSingleOrder(@PathVariable Long id ,Model model){
        System.out.println("id"+id);



        Order order = orderService.getOrderById(id);

        List<OrderItems> getOrderItemByOrder = orderService.getOrderById(id).getOrderItems();


        System.out.println(getOrderItemByOrder+"jd");


        model.addAttribute("orderItems",getOrderItemByOrder);
        model.addAttribute("order",order);
        return "admin/view-order";
    }


    @GetMapping("/status")
    @ResponseBody
    public List<Status> getStatus(){
        return Arrays.asList(Status.values());
    }

    @PostMapping("/order/status_change/{orderId}")
    public String updateStatus(@PathVariable Long orderId ,
                               @RequestParam Status orderStatus){

        System.out.println("Longhhh"+orderId);
        System.out.println("sjfgbsdjk"+orderStatus);

        try{
            orderService.updateOrderStatus(orderId,orderStatus);
            return "redirect:/orders/user-orders";
        }catch (Exception e){
            return "product-not-found";
        }

    }



}
