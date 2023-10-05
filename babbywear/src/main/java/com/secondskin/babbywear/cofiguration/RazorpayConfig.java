package com.secondskin.babbywear.cofiguration;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class RazorpayConfig {

    @Value("${razorpay.api.key.id}")
    private String razorpayKey;

    @Value("${razorpay.api.key.secret}")
    private String razorpaySecret;
}
