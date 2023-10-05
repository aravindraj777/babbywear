package com.secondskin.babbywear.controller.admin;




import com.secondskin.babbywear.model.Order;
import com.secondskin.babbywear.model.SalesTime;
import com.secondskin.babbywear.repository.OrderRepository;
import com.secondskin.babbywear.service.pdfGenerator.PdfGeneratorService;
import com.secondskin.babbywear.service.sales.SalesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/sales")
public class SalesController {


    @Autowired
    SalesService salesService;

    @Autowired
    OrderRepository orderRepository;



    @Autowired
    PdfGeneratorService pdfGeneratorService;


    @GetMapping("/dashboard")
    public String salesReport(Model model){



        BigDecimal weeklyTotal = salesService.calculateWeeklyOrderSum();
        long weekOrderCount = salesService.calculateWeeklyOrderCount();

         long dailyOrderCount =  salesService.calculateDailyOrderCount(LocalDate.now());
         BigDecimal dailyOrderTotal = salesService.calculateDailyOrderSum(LocalDate.now());

         long monthlyOrderCount = salesService.calculateMonthlyOrderCount();
         BigDecimal monthlyOrderTotal = salesService.calculateMonthlyOrderSum();

         long yearlyOrderCount = salesService.calculateYearlyOrderCount();
         BigDecimal yearlyOrderTotal = salesService.calculateYearlyOrderSum();


         long deliveredOrderCount  = salesService.countDeliveredOrders();


         model.addAttribute("deliveredOrderCount",deliveredOrderCount);
         model.addAttribute("yearlyOrderCount",yearlyOrderCount);
         model.addAttribute("yearlyOrderTotal",yearlyOrderTotal);
         model.addAttribute("monthOrderCount",monthlyOrderCount);
         model.addAttribute("monthlyOrderTotal",monthlyOrderTotal);
         model.addAttribute("dailyOrderTotal",dailyOrderTotal);
         model.addAttribute("dailyOrderCount",dailyOrderCount);
         model.addAttribute("weeklyTotal",weeklyTotal);
         model.addAttribute("weeklyOrder",weekOrderCount);



        return "admin/dashboard";
    }


    @PostMapping("/sales-by-selectedDate")
    public String orderByDateBetween(@RequestParam("startDate")@DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate startDate,
                                     @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate endDate,
                                     HttpSession session,Model model){


        List<Order> orders = salesService.getOrderBySelectedDate(startDate,endDate);
          float totalSales =   salesService.calculateTotalSales(orders);
          int totalOrders = orders.size();


          String token = UUID.randomUUID().toString();
          session.setAttribute(token,orders);


          model.addAttribute("totalOrders",totalOrders);
          model.addAttribute("orders",orders);
          model.addAttribute("totalSales",totalSales);
          model.addAttribute("token",token);


          return "admin/sales-report";
    }


    @PostMapping("/sales-by-periods")
    public String getSalesByPeriods(@RequestParam (value = "selectedTimePeriod")SalesTime salesTime,
                                    HttpSession session,Model model){

        if(salesTime != null){
            List<Order> orders = salesService.findByTimePeriod(salesTime);
             float  totalSales= salesService.calculateTotalSales(orders);

             int totalOrders = orders.size();

             String token = UUID.randomUUID().toString();


            session.setAttribute(token, orders);

            model.addAttribute("totalOrders", totalOrders);
            model.addAttribute("orders", orders);
            model.addAttribute("totalSales", totalSales);
            model.addAttribute("token", token);
            return "admin/sales-report";

        }

        else {
            model.addAttribute("message"," select a time period");
            return "redirect:/sales/dashboard";
        }





    }


    @GetMapping("/generate-pdf")
    public void generatePdf(HttpServletResponse response, HttpSession session,
                              HttpServletRequest request){

        String token = request.getParameter("token");
        List<Order> orders = (List<Order>) session.getAttribute(token);
        pdfGeneratorService.generatePdf(orders,response);
    }





}
