package com.secondskin.babbywear.repository;

import com.secondskin.babbywear.model.UserInfo;
import com.secondskin.babbywear.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<Wallet,Long> {


        Wallet findByUserInfo(UserInfo userInfo);

        Wallet findByUserInfoUserName(String userName);


}
