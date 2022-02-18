package com.example.Task3.Task3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.sql.Date;
import java.time.LocalDate;

public class CustomUserDetailsService implements UserDetailsService {

    @Autowired    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        Date date = Date.valueOf(LocalDate.now());
        user.setLastLogin(date);
        userRepository.save(user);
        return new CustomUserDetails(user);
    }
}
