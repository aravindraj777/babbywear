package com.secondskin.babbywear.service.email;

import com.secondskin.babbywear.model.Variant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailServiceImpl implements EmailService{

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String mail;

    @Override
    public void sendAdminLowStockNotification(Variant variant) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("Low Stock Notification");

        message.setText("variant"+variant.getVariantName() +"of product"+variant.getProducts().getProductName()+"in low stock");
        message.setTo(mail);


        javaMailSender.send(message);



    }
}
