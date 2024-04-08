package com.gens.usermanagement.service;

import com.gens.common.exceptionHandling.CustomResponseException;
import com.gens.common.exceptionHandling.ErrorCodeEnum;
import com.gens.usermanagement.model.document.CustomUserDetails;
import com.gens.usermanagement.model.document.User;
import com.gens.usermanagement.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HttpServletRequest request;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> fetchedUser = userRepository.findByEmail(username);
        if(fetchedUser.isEmpty()){
            log.error("User not found with email: {}", username);
            throw new CustomResponseException(ErrorCodeEnum.ER1003, HttpStatus.UNAUTHORIZED);
        }
        return new CustomUserDetails(fetchedUser.get());
    }
}