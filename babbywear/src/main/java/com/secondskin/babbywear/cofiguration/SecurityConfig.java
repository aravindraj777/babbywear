package com.secondskin.babbywear.cofiguration;

import com.secondskin.babbywear.service.user.UserInfoDetailsService;
import com.secondskin.babbywear.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Arrays;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class SecurityConfig {



    @Autowired
    UserInfoDetailsService userInfoDetailsService;




    @Bean
    public AuthenticationProvider authenticationProvider(){

        DaoAuthenticationProvider authenticationProvider =new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userInfoDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }



    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)throws Exception{

        http.csrf().disable()
                .authorizeRequests(authorize->authorize
                        .antMatchers("/static/**","/assets/**","/productImages/**").permitAll()
                        .antMatchers("/signup","/","/forget-pass","/resetPass","/updatePassword","/resetOtp")
                        .permitAll()
                        .anyRequest().authenticated())
                .formLogin(form->form
                        .loginPage("/login")
                        .permitAll()
                        .defaultSuccessUrl("/",true)

                ).exceptionHandling(exception->exception
                        .accessDeniedHandler(((request, response, accessDeniedException) -> response.sendRedirect("/access-denied"))
                        )
                ).formLogin(form->form
                        .successHandler(((request, response, authentication) -> {
                            for (GrantedAuthority auth : authentication.getAuthorities()){
                                    if(auth.getAuthority().equals("ROLE_ADMIN")){
                                        response.sendRedirect("/sales/dashboard");
                                        return;
                                    }
                            }
                            response.sendRedirect("/");
                        })));
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }





}
