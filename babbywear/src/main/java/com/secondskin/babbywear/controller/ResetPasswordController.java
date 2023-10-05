package com.secondskin.babbywear.controller;


import com.secondskin.babbywear.dto.ResetPasswordDTO;
import com.secondskin.babbywear.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller

public class ResetPasswordController {

    @Autowired
    UserService userService;


    @GetMapping("/forget-pass")
    public String forgetPass(){
        return "mail-otp";
    }

    @PostMapping("/resetPass")
    public String resetPass(@ModelAttribute ResetPasswordDTO resetPasswordDTO){

        userService.forgetPass(resetPasswordDTO);
        return "reset-otp";
    }




    @PostMapping("/resetOtp")
    public String inputOtp(@ModelAttribute ResetPasswordDTO resetPasswordDTO){

        boolean res= userService.verifyEmail(resetPasswordDTO);
        if (res){

            return "resetPassword";
        }
        else {

            return "reset-otp";
        }
    }


    @PostMapping("/updatePassword")
    public String updatePassword(@ModelAttribute ResetPasswordDTO resetPassDto){

        userService.passwordUpdate(resetPassDto);

            return "login";

    }






}
