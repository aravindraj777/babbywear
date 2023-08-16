package com.secondskin.babbywear.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {

    private String firstName;
    private String lastName;
    private String userName;
    private Long phone;
    private String email;
    private String password;
    private String confirmPassword;
}
