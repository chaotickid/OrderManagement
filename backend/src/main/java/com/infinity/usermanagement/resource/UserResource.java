package com.infinity.usermanagement.resource;

import com.infinity.usermanagement.model.document.User;
import com.infinity.usermanagement.model.view.JWTToken;
import com.infinity.usermanagement.model.view.UserVM;
import com.infinity.usermanagement.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usermanagement")
@Slf4j
public class UserResource {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody UserVM signUpRequest) {
        log.debug("User sign up requested =>");
        User systemUser1 = userService.addUser(signUpRequest);
        log.debug("Successfully created user for email: {}", systemUser1.getEmail());
        return new ResponseEntity<>(systemUser1, HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<JWTToken> signin(@RequestBody UserVM signInRequest) {
        log.debug("User sign in requested =>");
        return new ResponseEntity<>(userService.getUserDetails(signInRequest), HttpStatus.OK);
    }
}