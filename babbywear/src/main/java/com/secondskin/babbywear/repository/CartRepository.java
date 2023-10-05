package com.secondskin.babbywear.repository;


import com.secondskin.babbywear.model.Cart;
import com.secondskin.babbywear.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {



    Optional<UserInfo> findByUserInfo(UserInfo userInfo);


}
