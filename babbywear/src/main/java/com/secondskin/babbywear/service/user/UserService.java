package com.secondskin.babbywear.service.user;

import com.secondskin.babbywear.dto.OtpDto;
import com.secondskin.babbywear.dto.ResetPasswordDTO;
import com.secondskin.babbywear.dto.UserDto;
import com.secondskin.babbywear.model.Address;
import com.secondskin.babbywear.model.Cart;
import com.secondskin.babbywear.model.Order;
import com.secondskin.babbywear.model.UserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {


    OtpDto saveUser(UserDto userDto);

    void save(UserInfo userInfo);

    List<UserInfo> findAll();

    UserInfo findByUsername(String username);

    Optional<UserInfo> getByUserName(String userName);

    UserInfo findById(Long id);

    UserInfo findByemail(String email);

    void blockById(Long id);

    void unBlockById(Long id);

    Page<UserInfo>findAll(int pageNo,int pageSize);

     UserInfo forgetPass(ResetPasswordDTO resetPasswordDTO);


    void addAddressToUser(UserInfo userInfo, Address newAddress);


    boolean verifyEmail(ResetPasswordDTO resetPassDto);

    UserInfo passwordUpdate(ResetPasswordDTO resetPassDto);

    List<Address> getUserAddress(String userName);

    void deleteCart(Cart cart);


    void updateUser(Long id, UserInfo updateUser);


    List<Order> getUserOrders(String userName);
}
