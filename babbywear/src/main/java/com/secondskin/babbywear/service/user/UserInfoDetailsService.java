package com.secondskin.babbywear.service.user;

import com.secondskin.babbywear.cofiguration.UserInfoToUserDetailsConversion;
import com.secondskin.babbywear.model.UserInfo;
import com.secondskin.babbywear.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserInfoDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserInfo> userInfo = userRepository.findByUserName(username);
        return userInfo.map(UserInfoToUserDetailsConversion::new)
                .orElseThrow(()->new UsernameNotFoundException("UserName Not found"+username));
    }



}
