package com.secondskin.babbywear.service.wallet;

import com.secondskin.babbywear.model.Wallet;

public interface WalletService {

    boolean walletTransfer(Long userId,float amount);

    Wallet findWalletByUserName(String userName);
}
