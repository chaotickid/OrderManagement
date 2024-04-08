package com.gens.usermanagement.resource;

import com.gens.usermanagement.model.document.User;
import com.gens.usermanagement.model.view.JWTToken;
import com.gens.usermanagement.model.view.UserVM;
import com.gens.usermanagement.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class UserResource {

    @Autowired
    private UserService userService;

    @PostMapping("/signup/admin")
    @PreAuthorize("hasPermission('USER-MANAGEMENT_CREATE-ADMIN','CREATE')")
    public ResponseEntity<UserVM> signUpAsAdmin(@RequestBody @Valid UserVM signUpRequest) {
        log.debug("User sign up requested =>");
        User systemUser1 = userService.createAdmin(signUpRequest);
        log.debug("Successfully created user for email: {}", systemUser1.getEmail());
        signUpRequest.setPassword(null);
        return new ResponseEntity<>(signUpRequest, HttpStatus.CREATED);
    }

    //permission is not required -> PUBLIC API's
    @PostMapping("/signup/client")
    public ResponseEntity<UserVM> signUp(@RequestBody @Valid UserVM signUpRequest) {
        log.debug("User sign up requested =>");
        User systemUser1 = userService.createClient(signUpRequest);
        log.debug("Successfully created user for email: {}", systemUser1.getEmail());
        signUpRequest.setPassword(null);
        return new ResponseEntity<>(signUpRequest, HttpStatus.CREATED);
    }

    //permission is not required -> PUBLIC API's
    @PostMapping("/signin/client")
    public ResponseEntity<JWTToken> signIn(@RequestBody @Valid UserVM signInRequest) {
        log.debug("User sign in requested =>");
        return new ResponseEntity<>(userService.getUserDetails(signInRequest), HttpStatus.OK);
    }
}