package com.innople.devpleyground.dpfpapi.common.security.custom;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(username.equals("admin"))
            return new CustomUserDetails(username);
        else
            throw new UsernameNotFoundException("Can not found user!");
    }
}
