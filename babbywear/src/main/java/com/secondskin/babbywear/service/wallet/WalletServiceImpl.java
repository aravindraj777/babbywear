package com.secondskin.babbywear.service.wallet;

import com.secondskin.babbywear.model.UserInfo;
import com.secondskin.babbywear.model.Wallet;
import com.secondskin.babbywear.repository.UserRepository;
import com.secondskin.babbywear.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WalletServiceImpl implements WalletService{


    @Autowired
    WalletRepository walletRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public boolean walletTransfer(Long userId, float amount) {


        UserInfo user = userRepository.findById(userId).orElse(null);
        if(user!=null){
            Wallet wallet = walletRepository.findByUserInfo(user);

            if(wallet == null){
                wallet = new Wallet();
                wallet.setUserInfo(user);
                wallet.setBalance(0.0f);
                walletRepository.save(wallet);

            }
            wallet.setBalance(wallet.getBalance() + amount);
            walletRepository.save(wallet);
            return true;
        }

        return false;
    }

    @Override
    public Wallet findWalletByUserName(String userName) {

        Wallet wallet = walletRepository.findByUserInfoUserName(userName);

        if(wallet == null){
            Wallet newWallet = new Wallet();
            newWallet.setBalance(0.0f);
            return newWallet;
        }

        return wallet;
    }
}
