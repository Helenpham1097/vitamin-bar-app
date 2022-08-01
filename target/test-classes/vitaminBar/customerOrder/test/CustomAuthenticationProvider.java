package com.vitaminBar.customerOrder.test;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;

public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Resource
    UserDetailsService userDetailsService;

    @Resource
    SecurityUserDetailsService service;

    @Resource
    PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // empty user name handler

        System.out.println(authentication.toString());
        final String username = (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : authentication.getName();

        if (username.isEmpty()) {
            throw new BadCredentialsException("invalid login details");
        }

        // unregistered account handler
        Users user = (Users) userDetailsService.loadUserByUsername(username);

        if (user == null) {
            throw new BadCredentialsException("Your account has not been registered. Please sign in");
        }

        //wrong password handler and correct account handler
        boolean checkedPassword = passwordEncoder.matches((CharSequence) authentication.getCredentials(), user.getPassword());

        if (checkedPassword && user.isAccountNonLocked() || checkedPassword && service.unlockedWhenTimeExpired(user)) {
            if(user.getFailedAttempts()>0) {
                service.resetFailedAttempt(user);
            }
            return createSuccessfulAuthentication(authentication, user);
        }
        else {
            throw new BadCredentialsException("invalid login details");
        }
    }

    public Authentication createSuccessfulAuthentication(Authentication authentication, UserDetails user) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUsername(), authentication.getCredentials(), user.getAuthorities());
        token.setDetails(authentication.getDetails());
        return token;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
