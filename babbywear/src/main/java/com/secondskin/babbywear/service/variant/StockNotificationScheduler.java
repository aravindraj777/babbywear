package com.secondskin.babbywear.service.variant;

import com.secondskin.babbywear.model.Variant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class StockNotificationScheduler {

    @Autowired
    private VariantService variantService;




    @Scheduled(cron = "0 26 13 * * ?")
    public void checkLowStockAndNotifyAdmin(){
        variantService.checkStockAndNotifyAdmin();
    }

}
