package com.secondskin.babbywear.controller.shop;


import com.secondskin.babbywear.model.UserInfo;
import com.secondskin.babbywear.repository.UserRepository;
import com.secondskin.babbywear.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.mail.search.SearchTerm;
import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/details")
public class UserDashboardController {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/user-account")
    public String getDashboard(Model model, Principal principal){
//        String username = principal.getName();
//        Optional<UserInfo> userInfo = userRepository.findByUserName(username);
//        model.addAttribute("users",userInfo);

        String username = principal.getName();
        Optional<UserInfo> userInfo = userRepository.findByUserName(username);

        if(userInfo.isPresent()){
            UserInfo userInfo1 = userInfo.get();
            model.addAttribute("users",userInfo1);
            return "user-dashboard";
        }
        else {
           return  "product-not-found";

        }


    }


//    public String getCurrentUserName(){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        return authentication.getName();
//    }
//

    @GetMapping("/update-user")
    public String updateUserInfo(){
        return "update-dashboard";
    }

}
