package com.secondskin.babbywear.controller.shop;


import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.secondskin.babbywear.model.*;
import com.secondskin.babbywear.repository.CartItemRepository;
import com.secondskin.babbywear.repository.CartRepository;
import com.secondskin.babbywear.repository.UserRepository;
import com.secondskin.babbywear.repository.VariantRepository;
import com.secondskin.babbywear.service.Address.AddressService;
import com.secondskin.babbywear.service.cart.CartService;
import com.secondskin.babbywear.service.cartItem.CartItemNotFoundException;
import com.secondskin.babbywear.service.cartItem.CartItemService;
import com.secondskin.babbywear.service.coupon.CouponService;
import com.secondskin.babbywear.service.order.OrderService;
import com.secondskin.babbywear.service.order.OrderServiceImpl;
import com.secondskin.babbywear.service.razorpay.RazorpayService;
import com.secondskin.babbywear.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller

public class CartController {

    @Autowired
    CartService cartService;

    @Autowired
    UserService userService;

    @Autowired
    CartItemService cartItemService;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    AddressService addressService;

    @Autowired
    OrderService orderService;
    @Autowired
    CartRepository cartRepository;

    @Autowired
    CouponService couponService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    VariantRepository variantRepository;

    @Autowired
    OrderServiceImpl orderServiceImpl;

    @Autowired
    RazorpayService razorpayService;

    private static String CURRENCY = "INR";







    @GetMapping ("/addToCart/{id}")
    @ResponseBody
    public ResponseEntity<String> addToCart(@PathVariable String id, Authentication authentication){


        try{
            Long variantId = Long.parseLong(id);
            System.out.println("1");


            Authentication currentAuthentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println(currentAuthentication+"current user");


            if(authentication!=null && authentication.isAuthenticated()){
                System.out.println("1.5");

                UserDetails userDetails =(UserDetails) authentication.getPrincipal();
                String username = userDetails.getUsername();
                ResponseEntity<String> response = cartService.addToCart(username,variantId);
                System.out.println("1.8");
                System.out.println("2");
                return response;
            }
            else {
                System.out.println("3");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User Not authenticated:Try Login");
            }
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Invalid  User");}
    }


    @GetMapping("/cart")
    public String viewCart(Model model, Principal principal){
        String userName = principal.getName();
        UserInfo user  = userService.findByUsername(userName);
        List<CartItem> cartItems = cartItemService.getCartItemsForUserCart(user.getCart());
        float cartTotal = 0.0f;
        for(CartItem cartItem:cartItems){
            cartTotal += cartItem.getVariant().getPrice();
        }


        model.addAttribute("cartItems",cartItems);
        model.addAttribute("cartTotal",cartTotal);
        return "cart";
    }

    @GetMapping("/delete/{id}")
    public String deleteCartItem(@PathVariable long id){
        cartService.deleteCartItemById(id);
        System.out.println("ajg"+id);
        return "redirect:/cart";
    }

//   price changing according to the quantity

    @PostMapping ("/update-quantity")
    @ResponseBody
    public ResponseEntity<Float> updateQuantity(@RequestParam Long cartItemId, @RequestParam int newQuantity){

        System.out.println("id"+cartItemId+" "+newQuantity);

        System.out.println("id"+cartItemId);
        CartItem cartItem = cartItemService.findById(cartItemId);
        cartItem.setQuantity(newQuantity);
        cartItemService.save(cartItem);

        float cartItemTotal = cartItemTotal(cartItem);

         return ResponseEntity.ok(cartItemTotal);
    }


//    Method - cartItem Total
    private float cartItemTotal(CartItem cartItem){
        Variant variant = cartItem.getVariant();
        float variantPrice = variant.getPrice();
        int quantity = cartItem.getQuantity();
        return quantity*variantPrice;
    }


//    CHECKOUT - CONTROLLING

    @GetMapping("/checkout")
    public String checkOut(Model model , Principal principal, HttpSession session) throws RazorpayException {

        String userName = principal.getName();
        UserInfo userInfo = userService.findByUsername(userName);
        Cart cart = userInfo.getCart();
        List<Address> address = userService.getUserAddress(userName);



        float cartTotal = cartItemService.calculateCartTotal(cart);
        float delTotal = cartTotal;
        List<Coupon> availableCoupons = couponService.getAvailableCoupons(cartTotal,userInfo.getId());

        Boolean couponApplied = (Boolean) session.getAttribute("couponApplied");
        Float updatedCartTotal = (Float) session.getAttribute("updatedCartTotal");

        if (couponApplied == null) {
            couponApplied = false;
        }

        Float amountSaved = couponApplied && updatedCartTotal != null ? cartTotal - updatedCartTotal : 0.0f;

        if (couponApplied != null && couponApplied && updatedCartTotal != null) {


            cartTotal = updatedCartTotal;
        }



          com.razorpay.Order order = razorpayService.createOrder(cartTotal,CURRENCY);

            String orderId = order.get("id");
            int amount= order.get("amount");


         System.out.println(order.toString()+"kjsbh");

         model.addAttribute("orderId",orderId);
         model.addAttribute("updatedCartTotal", updatedCartTotal);
         model.addAttribute("couponApplied", couponApplied);
         model.addAttribute("address",address);
         model.addAttribute("orderAmount",amount);
         model.addAttribute("cartTotal",cartTotal);
         model.addAttribute("delTotal",delTotal);
        model.addAttribute("amountSaved", amountSaved);
         model.addAttribute("cartItems",cart.getCartItems());
        return "checkout";
    }

    @PostMapping("/place-order")
    public String placeOrder(@RequestParam("shippingAddress") Long id,
                             @RequestParam("selectedPaymentMethod")Payment selectedPaymentMethod,
                             HttpServletRequest request,HttpSession session,
                             Model model ){

        System.out.println(id);
        System.out.println(selectedPaymentMethod);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication!=null && authentication.getPrincipal()instanceof UserDetails){
            UserDetails userDetails =((UserDetails) authentication.getPrincipal());
            String userName = userDetails.getUsername();
            System.out.println(userName);

            UserInfo userInfo = userRepository.findByUserName(userName)
                  .orElseThrow(()->new UsernameNotFoundException("Not found"));

            Cart cart = userInfo.getCart();
            List<CartItem> cartItems =cart.getCartItems();
            float totalAmount = calculateTotalOrderAmount(cartItems);

//            float cartTotal = cartItemService.calculateCartTotal(cart);
            Boolean couponApplied = (Boolean) session.getAttribute("couponApplied");
            Float updatedCartTotal = (Float) session.getAttribute("updatedCartTotal");

            if (couponApplied != null && couponApplied && updatedCartTotal != null) {
               totalAmount = updatedCartTotal;
            }
            else totalAmount = totalAmount;


            Address shippingAddress = addressService.getAddressById(id);
//            Payment payment = Payment.valueOf(String.valueOf(selectedPaymentMethod));
            Status orderStatus = Status.ORDER_PENDING;
            

            if(selectedPaymentMethod.equals(Payment.COD)) {
                Order order = new Order();
                order.setUserInfo(userInfo);
                order.setAddress(shippingAddress);
                order.setPayment(selectedPaymentMethod);
                order.setStatus(orderStatus);
                order.setTotal(totalAmount);

                List<OrderItems> orderItemsList = new ArrayList<>();
                for (CartItem cartItem : cartItems) {
                    OrderItems orderItems = new OrderItems();
                    orderItems.setVariant(cartItem.getVariant());
                    orderItems.setQuantity(cartItem.getQuantity());
                    orderItems.setPrice(cartItem.getVariant().getPrice());
                    order.setOrderedDate(LocalDate.now());
                    orderItems.setOrder(order);
                    orderItemsList.add(orderItems);

                }

                for (CartItem cartItem : cartItems) {
                    Variant variant = cartItem.getVariant();
                    int quantity = cartItem.getQuantity();
                    int stock = variant.getStock();
                    if (quantity <= stock) {
                        variant.setStock(stock - quantity);
                        order.setOrderItems(orderItemsList);
                        order.setStatus(Status.ORDER_CONFIRMED);
                        order.setOrderNumber(order.getOrderNumber());
                        orderService.createOrder(order);

                        String successMessage = String.valueOf(Status.ORDER_CONFIRMED);
                        model.addAttribute("successMessage", successMessage);
                        variantRepository.save(variant);

                        cartItems.clear();
                        cartService.saveCart(cart);
                        userService.deleteCart(cart);
                        System.out.println("hekkkk");
                        cartService.deleteCart(cart);

                        System.out.println("lkjdasgkj");
                        session.removeAttribute("cartTotal");
                        session.removeAttribute("couponApplied");
                        session.removeAttribute("updatedCartTotal");
                        return "order-page";
                    } else {
                        throw new CartItemNotFoundException("jbsdjv");
                    }


                 }
            }else if(selectedPaymentMethod.equals(Payment.ONLINE)){

                Order order = new Order();
                order.setUserInfo(userService.findByUsername(userName));
                order.setAddress(shippingAddress);
                order.setPayment(selectedPaymentMethod);
                order.setStatus(orderStatus);
                order.setOrderedDate(LocalDate.now());
                order.setTotal(totalAmount);

//            float totalAmount = 0.0f;
                List<OrderItems> orderItemsList = new ArrayList<>();
                for (CartItem cartItem : cartItems) {
                    OrderItems orderItems = new OrderItems();
                    orderItems.setVariant(cartItem.getVariant());
                    orderItems.setQuantity(cartItem.getQuantity());
                    orderItems.setPrice(cartItem.getVariant().getPrice());
                    orderItems.setOrder(order);

                    orderItemsList.add(orderItems);


                }
                 for (CartItem cartItem : cartItems) {
                    Variant variant = cartItem.getVariant();
                    int quantity = cartItem.getQuantity();

                    if (quantity <= variant.getStock()) {
                        variant.setStock(variant.getStock() - quantity);
                        order.setOrderItems(orderItemsList);
                        order.setOrderNumber(order.getOrderNumber());
                        orderService.createOrder(order);

                        variantRepository.save(variant);

                        String successMessage = String.valueOf(Status.ORDER_CONFIRMED);

                        model.addAttribute("successMessage", successMessage);

                        cartItems.clear();
                        cartService.saveCart(cart);
                        userService.deleteCart(cart);
                        System.out.println("hekkkk");
                        cartService.deleteCart(cart);

                        System.out.println("lkjdasgkj");
                        session.removeAttribute("cartTotal");
                        session.removeAttribute("couponApplied");
                        session.removeAttribute("updatedCartTotal");
                        return "order-page";

                    }


                    else {
                        throw new CartItemNotFoundException("jbsdjv");
                    }

                 }
            }
        }

        return "redirect:/checkout";

    }


//    Helper methods

    private float calculateTotalOrderAmount(List<CartItem> cartItems){
        float totalAmount = 0.0f;

        for(CartItem cartItem: cartItems)
        {
            float itemPrice = cartItem.getVariant().getPrice();
            int quantity = cartItem.getQuantity();
            totalAmount +=  itemPrice * quantity;
        }
        return totalAmount;
    }



    @GetMapping({"/createTransaction/{amount}"})
    public void createTransaction(@PathVariable(name = "amount") Double amount){

        orderServiceImpl.createTransaction(amount);

    }























}
