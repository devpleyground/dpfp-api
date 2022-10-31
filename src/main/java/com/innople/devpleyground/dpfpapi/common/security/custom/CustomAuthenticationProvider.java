package com.innople.devpleyground.dpfpapi.common.security.custom;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Log4j2
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private UserDetailsService userDetailsService;
    private BCryptPasswordEncoder passwordEncoder;

    public CustomAuthenticationProvider(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.passwordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        CustomUserDetailService customUserDetailService = (CustomUserDetailService)userDetailsService;
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
        // AuthenticaionFilter에서 생성된 토큰으로부터 아이디와 비밀번호를 조회함
        String userName = token.getName();
        String userPassword = (String) token.getCredentials();

        CustomUserDetails userDetails = (CustomUserDetails) customUserDetailService.loadUserByUsername(userName);

        if (!passwordEncoder.matches(userPassword, userDetails.getPassword())) {
            throw new BadCredentialsException(userDetails.getUsername() + "Invalid password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, userPassword, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
