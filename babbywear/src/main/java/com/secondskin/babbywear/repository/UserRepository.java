package com.secondskin.babbywear.repository;


import com.secondskin.babbywear.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserInfo,Long> {

    Optional<UserInfo> findByUserName(String userName);

    Optional<UserInfo> findByEmail(String email);
    Optional<UserInfo> findByPhone(Long phoneNumber);

    Optional<UserInfo> findById(Long id);




}
