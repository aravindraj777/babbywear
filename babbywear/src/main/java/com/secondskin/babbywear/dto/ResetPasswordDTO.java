package com.secondskin.babbywear.dto;


import lombok.*;

import javax.annotation.processing.Generated;

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
