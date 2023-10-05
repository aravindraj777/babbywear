package com.secondskin.babbywear.controller.shop;


import com.secondskin.babbywear.model.*;
import com.secondskin.babbywear.repository.AddressRepository;
import com.secondskin.babbywear.repository.UserRepository;
import com.secondskin.babbywear.service.Address.AddressService;
import com.secondskin.babbywear.service.order.OrderService;
import com.secondskin.babbywear.service.stock.StockService;
import com.secondskin.babbywear.service.user.UserService;
import com.secondskin.babbywear.service.wallet.WalletService;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;


import java.security.Principal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/details")
public class UserDashboardController {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AddressService addressService;

    @Autowired
    OrderService orderService;

    @Autowired
    WalletService walletService;

    @Autowired
    StockService stockService;

    @Autowired
    AddressRepository addressRepository;


    public String getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @GetMapping("/user-account")
    public String getDashboard(Model model) {


        UserInfo user = userService.getByUserName(getCurrentUser()).orElse(null);
        model.addAttribute("users",user);
        return "user-dashboard";

    }




    @GetMapping("/update-user/{id}")
    public String updateUserInfo(@PathVariable("id") Long id,Model model){

        Optional<UserInfo> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()){
            UserInfo user = optionalUser.get();
            model.addAttribute("user",user);
            return "update-dashboard";
        }
        else {
            return "product-not-found";
        }


    }

    @PostMapping("/users/update")
    public String updateUser(@ModelAttribute("user") UserInfo updatedUser) {
        userService.updateUser(updatedUser.getId(),updatedUser);
        return "redirect:/details/user-account";
    }



    @PostMapping("/add-address")
    public String addAddressToUser(@AuthenticationPrincipal UserDetails userDetails,
                                   @ModelAttribute Address address){

        String username = userDetails.getUsername();
        Optional<UserInfo> userInfo =   userRepository.findByUserName(username);
        if(userInfo.isPresent()){
            UserInfo userInfo1 = userInfo.get();
            userService.addAddressToUser(userInfo1,address);
        }
        return "redirect:/details/user-account";

    }

    @GetMapping("/add-address")
    public String addAddress(Model model,Principal principal){
       model.addAttribute("addAddress",new Address());
       return "user-addresses";

    }




    @GetMapping("/show-address")
    public String showAddresses(Model model,Principal principal){

        List<Address> userAddress = userService.getUserAddress(principal.getName()).stream().
                                        filter(address -> !address.isDeleted()).toList();

        model.addAttribute("userAddress",userAddress);
        return "show-addresses";
    }


    @PostMapping("/edit-address")
    public String editAddress(@ModelAttribute("address")Address address,Model model){


        addressService.updateAddress(address,address.getId());

        return "redirect:/details/show-address";

    }

    @GetMapping("/edit-address/{id}")
    public String editUserAddress(@PathVariable Long id,Model model) {

        Address address = addressService.getAddressById(id);

        model.addAttribute("address",address);

        return "user-edit-address";
    }


    @GetMapping("/orders")
    public String getUserOrders(Principal principal,Model model){

        String userName = principal.getName();
        List<Order> orders = userService.getUserOrders(userName);
        model.addAttribute("orders",orders);

        return "user-orders";
    }


    @GetMapping("/view-order/{id}")
    public String userViewOrder(@PathVariable Long id,Model model,Principal principal){

        UserInfo user =  userService.findByUsername(principal.getName());

        Optional <Order> viewOrder = orderService.findOrderById(id);
        if(viewOrder.isPresent()){
            Order order = viewOrder.get();
            model.addAttribute("order",order);
            model.addAttribute("user",user);
            return "user-view-order";

        }
        else {
            return "product-not-found";
        }
    }




    @GetMapping("/getOrderStatus/{id}")
    @ResponseBody
    public ResponseEntity<String> getOrderStatus(@PathVariable Long id){

        String orderStatus = String.valueOf(orderService.getOrderStatusById(id));
        if (orderStatus != null){
            return ResponseEntity.ok(orderStatus);
        }else {
            return ResponseEntity.notFound().build();
        }
    }



    @PostMapping("/cancel-order")
    @ResponseBody
    public Map<String,String> cancelOrder(@RequestParam ("orderId") Long orderId) {



        Map<String, String> response = new HashMap<>();

        Order order = orderService.getOrderById(orderId);

        if (order != null) {
            if (order.getStatus() != Status.ORDER_CANCELLED) {

                if (order.getPayment() == Payment.COD) {
                    order.setStatus(Status.ORDER_CANCELLED);
                    order.setCancelledDate(LocalDate.now());
                    stockService.updateStockForOrderCancellation(order);
                    orderService.updateOrder(order);

                } else if (order.getPayment() == Payment.ONLINE) {
                    boolean transferToWallet = walletService.walletTransfer(order.getUserInfo().getId(), order.getTotal());
                    if (!transferToWallet) {
                        response.put("status", "error");
                        response.put("message", "unable to transfer To wallet");
                        return response;
                    }
                    order.setStatus(Status.ORDER_CANCELLED);
                    order.setCancelledDate(LocalDate.now());
                } else {
                    response.put("status", "error");
                    response.put("message", "PaymentMethod Unsupported");
                }
                stockService.updateStockForOrderCancellation(order);
                orderService.updateOrder(order);
                response.put("status", "success");

                response.put("message", "Order has been successfully canceled.");
            } else {
                response.put("status", "error");
                response.put("message", "The order is already canceled.");
            }
        } else {
            response.put("status", "error");
            response.put("message", "Unable to cancel the order.");
        }
        return response;
    }

    @GetMapping("/delete-address/{id}")

    public String deleteUserAddress(@PathVariable Long id){


        Address address = addressService.getAddressById(id);



        if(address !=null) {
            addressService.deleteById(id);
            return "redirect:/details/show-address";
        }
        else {
            return "product-not-found";
        }


    }






}


