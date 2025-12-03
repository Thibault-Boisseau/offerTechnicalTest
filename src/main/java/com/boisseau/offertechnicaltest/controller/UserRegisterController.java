package com.boisseau.offertechnicaltest.controller;

import com.boisseau.offertechnicaltest.model.dto.UserCreationRequest;
import com.boisseau.offertechnicaltest.model.dto.UserResponse;
import com.boisseau.offertechnicaltest.service.UserRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class UserRegisterController {

    @Autowired
    private UserRegisterService userRegisterService;

    @PostMapping("/users")
    public ResponseEntity<?> addUser(@RequestBody UserCreationRequest request){

        UserResponse saveUser = userRegisterService.addUser(request);
        return new ResponseEntity<>(saveUser, HttpStatus.CREATED);

    }

}
