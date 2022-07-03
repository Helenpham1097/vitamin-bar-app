package com.vitaminBar.customerOrder.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Service
public class SecurityUserDetailsService implements UserDetailsService {

    //attempts log in check
    public static final int MAX_FAILED_ATTEMPT = 3;
    public static final long LOCK_TIME_DURATION = 5 * 60 * 1000;

    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityUserDetailsService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    // check user account
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userRepository.findUserByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Your account has not been registered. Please sign in"));
    }

    // update failed attempts
    @Transactional
    public void increasedFailedAttempt(Users user) {
        int newFailedAttempts = user.getFailedAttempts() + 1;
        userRepository.updateFailedAttempts(newFailedAttempts, user.getUserName());
    }


    @Transactional
    public void resetFailedAttempt(Users user) {
        userRepository.updateFailedAttempts(0, user.getUsername());
    }

    public void lock(Users user) {
        user.setFailedAttempts(3);
        user.setAccountNonLocked(false);
        user.setLockTime(new Timestamp(System.currentTimeMillis()));

        userRepository.save(user);
    }

    public boolean unlockedWhenTimeExpired(Users user) {
        long lockTimeInMillis = user.getLockTime().getTime();
        long currentTimeInMillis = System.currentTimeMillis();

        if (lockTimeInMillis + LOCK_TIME_DURATION < currentTimeInMillis) {
            user.setAccountNonLocked(true);
            user.setFailedAttempts(0);
            user.setLockTime(null);

            userRepository.save(user);
            return true;
        }
        return false;
    }

    public void createUser(UserDetails user) {
        userRepository.save((Users) user);
    }
}
