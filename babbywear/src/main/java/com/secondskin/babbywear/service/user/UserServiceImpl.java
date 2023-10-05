package com.secondskin.babbywear.service.user;

import com.secondskin.babbywear.dto.OtpDto;
import com.secondskin.babbywear.dto.ResetPasswordDTO;
import com.secondskin.babbywear.dto.UserDto;
import com.secondskin.babbywear.model.*;
import com.secondskin.babbywear.otp.EmailUtil;
import com.secondskin.babbywear.otp.OtpUtil;
import com.secondskin.babbywear.repository.AddressRepository;
import com.secondskin.babbywear.repository.OrderRepository;
import com.secondskin.babbywear.repository.RoleRepository;
import com.secondskin.babbywear.repository.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Data
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    PasswordEncoder passwordEncoder;
    int flag = 0;

    @Autowired
    OtpUtil otpUtil;

    @Autowired
    EmailUtil emailUtil;

    @Autowired
    OrderRepository orderRepository;





//    @Override
//    public  boolean saveUser(UserDto userDto) {
//        Optional<UserInfo> userByUserName = userRepository.findByUserName(userDto.getUserName());
//        Optional<UserInfo> userByEmail = userRepository.findByEmail(userDto.getEmail());
//        Optional<UserInfo> userByPhone = userRepository.findByPhone(userDto.getPhone());
//
//        if(userByUserName.isPresent() || userByEmail.isPresent() || userByPhone.isPresent()){
//            flag  = 1;
//            return false;
//        }
//        else {
//            UserInfo userInfo = new UserInfo();
//            userInfo.setFirstName(userDto.getFirstName());
//            userInfo.setLastName(userDto.getLastName());
//            userInfo.setUserName(userDto.getUserName());
//            userInfo.setEmail(userDto.getEmail());
//            userInfo.setPhone(userDto.getPhone());
//            userInfo.setPassword(passwordEncoder.encode(userDto.getPassword()));
//            userInfo.setEnabled(true);
//
//
//
//            Role role = roleRepository.roleName("ROLE_USER");
//            userInfo.setRole(role);
//            userRepository.save(userInfo);
//
//            return true;
//        }
//
//    }


    @Override
    public OtpDto saveUser(UserDto userDto) {

        Optional<UserInfo> userByUsername = userRepository.findByUserName(userDto.getUserName());
        Optional<UserInfo> userByEmail = userRepository.findByEmail(userDto.getEmail());
        Optional<UserInfo> userByPhone =userRepository.findByPhone(userDto.getPhone());

        if(userByUsername.isPresent() || userByEmail.isPresent() || userByPhone.isPresent()){
            System.out.println("User already Exists");
        } else if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            System.out.println("Password Mismatch");

        }String otp = otpUtil.generateOtp();
        try{
            emailUtil.sendOtpEmail(userDto.getEmail(),otp);
        } catch (MessagingException e) {
            throw new RuntimeException("Email Not Send");
        }

        Role role = roleRepository.roleName("ROLE_USER");



        UserInfo newUser = UserInfo.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .userName(userDto.getUserName())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .email(userDto.getEmail())
                .phone(userDto.getPhone())
                .otpGeneratedTime(LocalDateTime.now())
                .role(role)
                .otp(otp)
                .enabled(true)
//                .isDeleted(true)
                .build();
        userRepository.save(newUser);

        OtpDto otpDto = new OtpDto()
                .builder()
                .id(newUser.getId())
                .email(newUser.getEmail())
                .status("Pending")
                .build();

        return otpDto;
    }



    public boolean verifyAccount(OtpDto otpDto) {
        System.out.println(otpDto);
        UserInfo user = userRepository.findById(otpDto.getId())

                .orElseThrow(() -> new RuntimeException("User not found with this email: " ));
        System.out.println(user);

        if (user.getOtp().equals(otpDto.getOtp()) && Duration.between(user.getOtpGeneratedTime(),
                LocalDateTime.now()).getSeconds() < (30 * 60)) {
            System.out.println(user.getOtp());
            user.setEnabled(true);


            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public void save(UserInfo userInfo) {

        Role role = roleRepository.roleName(userInfo.getRole().getRoleName());
        userInfo.setRole(role);
        userRepository.save(userInfo);
    }

    @Override
    public List<UserInfo> findAll() {
        return userRepository.findAll();
    }

    @Override
    public UserInfo findByUsername(String username) {
        Optional<UserInfo> userOptional = userRepository.findByUserName(username);
        if (userOptional.isPresent()) {
            System.out.println(userOptional);
            return userOptional.get();
        } else {
            throw new NoSuchElementException("User not found with username: " + username);
        }
    }

    @Override
    public Optional<UserInfo> getByUserName(String userName) {


        return userRepository.findByUserName(userName);
    }

    @Override
    public UserInfo findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public UserInfo findByemail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }


    @Override
    public void blockById(Long id){
        Optional<UserInfo>user = userRepository.findById(id);
        user.ifPresent(userInfo->{
            userInfo.setEnabled(false);
            userRepository.save(userInfo);
        });

    }

    @Override
    public void unBlockById(Long id) {
        Optional<UserInfo>user = userRepository.findById(id);
        user.ifPresent(userInfo -> {
            userInfo.setEnabled(true);
            userRepository.save(userInfo);
        });
    }

    @Override
    public Page<UserInfo> findAll(int pageNo,int pageSize) {
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        return userRepository.findAll(pageable);
    }



    @Override
    public UserInfo forgetPass(ResetPasswordDTO resetPasswordDTO) {
        Optional<UserInfo> userEmail = userRepository.findByEmail(resetPasswordDTO.getEmail());
        System.out.println(userEmail+"anbfsn");

        if (userEmail.isPresent()) {
            UserInfo user = userEmail.get();
            String otp = otpUtil.generateOtp();
            user.setOtp(otp);

            try {
                emailUtil.sendOtpEmail(resetPasswordDTO.getEmail(), otp);
            } catch (MessagingException e) {
                throw new RuntimeException("unable to create otp");
            }
            return userRepository.save(user);

        } else {
            throw new UsernameNotFoundException("gsg");
//            System.out.println("Email not present");
        }

    }


    public boolean verifyEmail(ResetPasswordDTO resetPassDto) {
        Optional<UserInfo> user = userRepository.findByEmail(resetPassDto.getEmail());

        UserInfo user1=user.get();
        if (user1.getOtp().equals(resetPassDto.getOtp())){
            System.out.println("otp success");
            return true;

        }

        return false;
    }




    @Override
    public UserInfo passwordUpdate(ResetPasswordDTO resetPassDto) {

        Optional<UserInfo> userPassword = userRepository.findByEmail(resetPassDto.getEmail());
        UserInfo user1=userPassword.get();
        System.out.println(user1);
        user1.setPassword(passwordEncoder.encode(resetPassDto.getPassword()));
        System.out.println("afkjsb");
        userRepository.save(user1);
        return null;
    }



    @Override
    @Transactional
    public void addAddressToUser(UserInfo userInfo, Address newAddress) {

        newAddress.setUserInfo(userInfo);
        addressRepository.save(newAddress);
        userInfo.getAddress().add(newAddress);
        userRepository.save(userInfo);
    }


//    public void saveDto(UserDto userDto) {
//        userRepository.save(userDto);
//    }


    @Override
    public List<Address> getUserAddress(String userName) {
        Optional<UserInfo> user = userRepository.findByUserName(userName);

        if(user.isPresent()){
           return user.get().getAddress();
        }
        else {
            return Collections.emptyList();
        }

    }

    @Override
    public void deleteCart(Cart cart) {

        UserInfo user = userRepository.findById(cart.getUserInfo().getId()).orElse(null);
        assert user != null;
        user.setCart(null);
        userRepository.save(user);
    }

    @Override
    public void updateUser(Long id, UserInfo updateUser) {
        Optional<UserInfo> getUser = userRepository.findById(id);
        if (getUser.isPresent()) {
            UserInfo user = getUser.get();
            user.setFirstName(updateUser.getFirstName());
            user.setLastName(updateUser.getLastName());
            user.setUserName(updateUser.getUserName());
            user.setEmail(updateUser.getEmail());
            user.setPhone(updateUser.getPhone());

            userRepository.save(user);
        }else {
            throw new UsernameNotFoundException("user not found");
        }
    }

    @Override
    public List<Order> getUserOrders(String userName) {

          return orderRepository.findByUserInfoUserName(userName);
    }

}
