package com.secondskin.babbywear.service.user;

import com.secondskin.babbywear.dto.OtpDto;
import com.secondskin.babbywear.dto.UserDto;
import com.secondskin.babbywear.model.Role;
import com.secondskin.babbywear.model.UserInfo;
import com.secondskin.babbywear.otp.EmailUtil;
import com.secondskin.babbywear.otp.OtpUtil;
import com.secondskin.babbywear.repository.RoleRepository;
import com.secondskin.babbywear.repository.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.time.Duration;
import java.time.LocalDateTime;
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
    PasswordEncoder passwordEncoder;
    int flag = 0;

    @Autowired
    OtpUtil otpUtil;

    @Autowired
    EmailUtil emailUtil;





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
            userInfo.setDeleted(true);
            userRepository.save(userInfo);
        });

    }

    @Override
    public void unBlockById(Long id) {
        Optional<UserInfo>user = userRepository.findById(id);
        user.ifPresent(userInfo -> {
            userInfo.setDeleted(false);
            userRepository.save(userInfo);
        });
    }

    @Override
    public Page<UserInfo> findAll(int pageNo,int pageSize) {
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        return userRepository.findAll(pageable);
    }


//    public void saveDto(UserDto userDto) {
//        userRepository.save(userDto);
//    }
}
