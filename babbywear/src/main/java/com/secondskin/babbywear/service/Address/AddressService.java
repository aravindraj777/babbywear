package com.secondskin.babbywear.service.Address;

import com.secondskin.babbywear.model.Address;
import com.secondskin.babbywear.model.UserInfo;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public interface AddressService {


    Address getAddressById(Long addressId);

    List<Address> getByUserName(String userName);


    void updateAddress(Address address,Long id);

    void  deleteById(Long id);
}
