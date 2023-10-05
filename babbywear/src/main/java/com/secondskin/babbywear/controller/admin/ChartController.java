package com.secondskin.babbywear.controller.admin;


import com.secondskin.babbywear.dto.ChartDTO;
import com.secondskin.babbywear.service.sales.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/chart")
public class ChartController {

    @Autowired
    SalesService salesService;



    @GetMapping("/show-chart")
    public ResponseEntity<List<ChartDTO>> getOrderCountAndDate(@RequestParam(name = "range", defaultValue = "weekly") String range) {
        System.out.println(range);
        List<ChartDTO> chartDataList = new ArrayList<>();

        switch (range) {
            case "weekly" -> chartDataList = salesService.calculateWeeklyOrderCountAndDate();
            case "monthly" -> chartDataList = salesService.calculateMonthlyOrderCountAndDate();
            case "yearly" -> chartDataList = salesService.calculateYearlyOrderCountAndDate();
            default -> {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(chartDataList);
            }
        }

        return ResponseEntity.ok(chartDataList);
    }







}
