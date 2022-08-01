package com.vitaminBar.customerOrder.security;

import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Resource
    private SecurityUserDetailsService userDetailsService;

    @Resource
    PasswordEncoder passwordEncoder;

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        AuthenticationException e)
            throws IOException, ServletException {


        // get username and password from login from
        String userNameProvidedFromLoginForm = httpServletRequest.getParameter("username");
        String passwordProvidedFromLoginForm = httpServletRequest.getParameter("password");

        // get user account from database
        Users user = (Users) userDetailsService.loadUserByUsername(httpServletRequest.getParameter("username"));

        if (user.getUsername().equals(userNameProvidedFromLoginForm)
                && !passwordEncoder.matches(passwordProvidedFromLoginForm, user.getPassword())) {

            if (user.isAccountNonLocked() && user.isEnabled()) {

                if (user.getFailedAttempts() < SecurityUserDetailsService.MAX_FAILED_ATTEMPT - 1) {
                    userDetailsService.increasedFailedAttempt(user);

                } else {
                    userDetailsService.lock(user);
                    e = new LockedException(" Your account will be locked for 5 minutes");
                }

            } else if (!user.isAccountNonLocked()) {

                if (!userDetailsService.unlockedWhenTimeExpired(user)) {
                    e = new LockedException("Your account has been locked. Please try again after 5 minutes");
                }
            }

        } else if (user.getUsername().equals(userNameProvidedFromLoginForm)
                && passwordEncoder.matches(passwordProvidedFromLoginForm, user.getPassword())) {

            if (!user.isAccountNonLocked()) {

                if(!userDetailsService.unlockedWhenTimeExpired(user)){

                    e = new LockedException("Your account has been locked. Please try again after 5 minutes");
                }
            }
        }
        System.out.println(e.getMessage());

        super.setDefaultFailureUrl("/login?error");
        super.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);
    }
}
