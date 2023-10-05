package com.secondskin.babbywear.dto;


import lombok.*;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ResetPasswordDTO {



    private String email;
    private String otp;
    private String password;
}
