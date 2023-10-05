package com.secondskin.babbywear.controller;


import com.secondskin.babbywear.dto.OtpDto;
import com.secondskin.babbywear.dto.UserDto;
import com.secondskin.babbywear.model.UserInfo;

import com.secondskin.babbywear.service.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class LoginController {

    @Autowired
    UserServiceImpl userService;

    @Autowired
    private AuthenticationManager authenticationManager;



    @GetMapping("/login")
    public String loginPage(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "login";
        }
        return "redirect:/";
    }



    @PostMapping("/login")
    public String processLoginForm(@ModelAttribute("authreq") UserDto userDto ,Model model){
        UserInfo user = userService.findByUsername(userDto.getUserName());

        if(user == null || !user.isEnabled()){
            model.addAttribute("error","Account Not enabled.Contact Administrator");
            return "login";
        }

        if(user.isDeleted()==false){

            return "login";
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDto.getUserName(),userDto.getPassword());
        try {
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return "redirect:/";
        }
        catch (AuthenticationException e){
            model.addAttribute("error","invalid credentials");
            return "login";
        }
    }




    @GetMapping("/signup")
    public String getSignUpPage(Model model) {
        model.addAttribute("signuprequest",new UserDto());
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute("signuprequest") UserDto userDto,Model model){
        OtpDto otp= userService.saveUser(userDto);
        model.addAttribute("otp",otp);
        return "otp-page";
    }

    @PostMapping("/verifyotp")
    public String verifyAccount(@ModelAttribute("otp") OtpDto otp) {



        boolean res=userService.verifyAccount(otp);
        if(res) {
            return "login";
        }else {

            return "otp-page";
        }


    }





}
