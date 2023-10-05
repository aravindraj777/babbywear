package com.secondskin.babbywear.service.email;


import com.secondskin.babbywear.model.Variant;

public interface EmailService {

    void sendAdminLowStockNotification(Variant variant);
}
