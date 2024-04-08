package com.gens.usermanagement.service;

import com.gens.common.constants.Constants;
import com.gens.common.exceptionHandling.CustomResponseException;
import com.gens.common.exceptionHandling.ErrorCodeEnum;
import com.gens.common.utils.JwtTokenProvider;
import com.gens.usermanagement.config.UserConfigs;
import com.gens.usermanagement.model.document.User;
import com.gens.usermanagement.model.view.JWTToken;
import com.gens.usermanagement.model.view.UserVM;
import com.gens.usermanagement.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class UserService {

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserConfigs userConfigs;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    /***
     * This method used to create user
     *
     * @param signUpRequest
     * @return
     */
    public User createClient(UserVM signUpRequest) {
        if (StringUtils.isBlank(signUpRequest.getEmail()) || StringUtils.isBlank(signUpRequest.getPassword())) {
            log.error("Either email id or password is blank");
            throw new CustomResponseException(ErrorCodeEnum.ER1002, HttpStatus.BAD_REQUEST);
        }
        Optional<User> alreadyHaveAUser = userRepository.findByEmail(signUpRequest.getEmail());
        if (alreadyHaveAUser.isPresent()) {
            log.error("User is already present with same email id: {}", signUpRequest.getEmail());
            throw new CustomResponseException(ErrorCodeEnum.ER1001, HttpStatus.CONFLICT);
        }
        User user = new User();
        try {
            user.setEmail(signUpRequest.getEmail());
            user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
            user.setLastLoginAt(String.valueOf(Instant.now()));
            user.setGivenName(signUpRequest.getGivenName());
            user.setFamilyName(signUpRequest.getGivenName());
            user.setIsAccountVerified(String.valueOf(Boolean.TRUE));
            user.setIsEmailVerified(String.valueOf(Boolean.FALSE));
            user.setIsAccountLocked(String.valueOf(Boolean.FALSE));
            user.setRole(Constants.CLIENT);
            user.setPrimaryDetails(signUpRequest.getPrimaryDetails());
            log.debug("Finalized used details: {}", user);
            userRepository.save(user);
        } catch (Exception e) {
            log.error("Unable to create a user. Reason: {}", e.getMessage());
            throw new CustomResponseException(ErrorCodeEnum.ER1000, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return user;
    }

    public User createAdmin(UserVM signUpRequest) {
        if (StringUtils.isBlank(signUpRequest.getEmail()) || StringUtils.isBlank(signUpRequest.getPassword())) {
            log.error("Either email id or password is blank");
            throw new CustomResponseException(ErrorCodeEnum.ER1002, HttpStatus.BAD_REQUEST);
        }
        Optional<User> alreadyHaveAUser = userRepository.findByEmail(signUpRequest.getEmail());
        if (alreadyHaveAUser.isPresent()) {
            log.error("User is already present with same email id: {}", signUpRequest.getEmail());
            throw new CustomResponseException(ErrorCodeEnum.ER1001, HttpStatus.CONFLICT);
        }
        User user = new User();
        try {
            user.setEmail(signUpRequest.getEmail());
            user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
            user.setLastLoginAt(String.valueOf(Instant.now()));
            user.setGivenName(signUpRequest.getGivenName());
            user.setFamilyName(signUpRequest.getGivenName());
            user.setIsAccountVerified(String.valueOf(Boolean.TRUE));
            user.setIsEmailVerified(String.valueOf(Boolean.FALSE));
            user.setIsAccountLocked(String.valueOf(Boolean.FALSE));
            user.setRole(Constants.CLIENT);
            user.setPrimaryDetails(signUpRequest.getPrimaryDetails());
            log.debug("Finalized used details: {}", user);
            userRepository.save(user);
        } catch (Exception e) {
            log.error("Unable to create a user. Reason: {}", e.getMessage());
            throw new CustomResponseException(ErrorCodeEnum.ER1000, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return user;
    }

    public User getUser(String email) {
        Optional<User> fetchedUser = Optional.empty();
        if (StringUtils.isBlank(email)) {
            log.error("User not found");
            throw new CustomResponseException(ErrorCodeEnum.ER1002, HttpStatus.BAD_REQUEST);
        }
        fetchedUser = userRepository.findByEmail(email);
        if (fetchedUser.isEmpty()) {
            log.error("User not found with email: {}", email);
            throw new CustomResponseException(ErrorCodeEnum.ER1003, HttpStatus.UNAUTHORIZED);
        }
        return fetchedUser.get();
    }

    public User getUserById(String customerId){
        Optional<User> fetchedUser = Optional.empty();
        if (StringUtils.isBlank(customerId)) {
            log.error("User not found");
            throw new CustomResponseException(ErrorCodeEnum.ER1006, HttpStatus.BAD_REQUEST);
        }
        fetchedUser = userRepository.findByCustomerId(userConfigs.getClassIdValue() + Constants.DOUBLE_COLON + customerId);
        if (fetchedUser.isEmpty()) {
            log.error("User not found with customer Id: {}", customerId);
            throw new CustomResponseException(ErrorCodeEnum.ER1003, HttpStatus.UNAUTHORIZED);
        }
        return fetchedUser.get();
    }

    public JWTToken getUserDetails(UserVM signInRequest) {
        Optional<User> fetchedUser = Optional.empty();
        if (StringUtils.isBlank(signInRequest.getEmail()) || StringUtils.isBlank(signInRequest.getPassword())) {
            log.error("Either email id or password is blank");
            throw new CustomResponseException(ErrorCodeEnum.ER1002, HttpStatus.BAD_REQUEST);
        }
        fetchedUser = userRepository.findByEmail(signInRequest.getEmail());

        if (fetchedUser.isEmpty()) {
            log.error("User not found with email: {}", signInRequest.getEmail());
            throw new CustomResponseException(ErrorCodeEnum.ER1003, HttpStatus.UNAUTHORIZED);
        }
        try {
            log.debug("Validated user name successfully");
            passwordVerification(signInRequest.getPassword(), fetchedUser.get());
            userRepository.save(fetchedUser.get());
            log.debug("Updated user values in the database successfully");
        } catch (Exception e) {
            log.error("Exception occurred while fetching user, Reason: {}", e.getMessage());
            throw new CustomResponseException(ErrorCodeEnum.ER1004, HttpStatus.UNAUTHORIZED);
        }
        JWTToken newJwtToken = new JWTToken();
        newJwtToken.setIdToken(jwtTokenProvider.createToken(fetchedUser.get()));
        return newJwtToken;
    }

    public void passwordVerification(String password, User user) {
        if (!passwordEncoder.matches(password, user.getPassword())) {
            log.debug("Incorrect password found");
            throw new CustomResponseException(ErrorCodeEnum.ER1004, HttpStatus.UNAUTHORIZED);
        }
    }

}