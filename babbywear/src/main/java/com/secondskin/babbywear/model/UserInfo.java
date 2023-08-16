package com.secondskin.babbywear.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Getter
@Setter
@Data
@Table(name ="user_info")
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;
    private String lastName;

    @Column(name = "userName")
    private String userName;

    private String email;

    @Column(name = "phoneNumber")
    private Long phone;

    private String password;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "role_id")
    private Role role;

    private boolean enabled;
//    private  boolean verified =true;

    private boolean isDeleted;

    private String otp;

    private LocalDateTime otpGeneratedTime;


    @OneToMany(mappedBy = "userInfo")
    private List<Address> address = new ArrayList<>();












}
