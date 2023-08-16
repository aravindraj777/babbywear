package com.secondskin.babbywear.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtpDto {

    private Long id;
    private String email;
    private  String status;
    private String otp;
}
