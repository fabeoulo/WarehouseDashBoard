/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.api.WebApiClient;
import com.advantech.api.WebApiUser;
import com.advantech.model.User;
import com.advantech.security.State;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author Wei.Cheng
 */
@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
    
    private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private WebApiClient wc;
    
    @Override
    public UserDetails loadUserByUsername(String jobnumber) throws UsernameNotFoundException {
        User user = userService.findByJobnumber(jobnumber);
        if (user == null) {
            WebApiUser atmcUser = wc.getUserInAtmc(jobnumber);
            if (atmcUser != null && atmcUser.getActive() == 1) {
                userService.saveUserWithNameByProc(atmcUser.getEmplr_Id(), atmcUser.getEmail_Addr(), atmcUser.getLocal_Name());
                user = userService.findByJobnumber(jobnumber);
            } else {
                log.debug("User :" + jobnumber + " not in Atmc");
                System.out.println("User not found");
                throw new UsernameNotFoundException("Username not found");
            }
        }
        
        user.addSecurityInfo(true, true, true, true, getGrantedAuthorities(user));
        return user;
    }
    
    private List<GrantedAuthority> getGrantedAuthorities(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        
        user.getUserProfiles().forEach((userProfile) -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + userProfile.getName()));
        });
        
        System.out.println("authorities :" + authorities);
        return authorities;
    }
    
}
