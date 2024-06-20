package com.secondskin.babbywear.controller.shop;


import com.secondskin.babbywear.model.Wallet;
import com.secondskin.babbywear.service.user.UserService;
import com.secondskin.babbywear.service.wallet.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/user-wallet")
public class WalletController {

    @Autowired
    WalletService walletService;

    @Autowired
    UserService userService;

    @GetMapping("/show-wallet")
    public String showUserWallet(Model model, Principal principal){
        String userName = principal.getName();
        Wallet wallet  = walletService.findWalletByUserName(userName);
        float balance = wallet.getBalance();
          model.addAttribute("balance",balance);
          model.addAttribute("wallet",wallet);

        return "Wallet";
    }
}
