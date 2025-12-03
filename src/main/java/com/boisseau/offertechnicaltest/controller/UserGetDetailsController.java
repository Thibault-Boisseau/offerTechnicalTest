package com.boisseau.offertechnicaltest.controller;

import com.boisseau.offertechnicaltest.model.dto.UserResponse;
import com.boisseau.offertechnicaltest.service.UserGetDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class UserGetDetailsController {

    @Autowired
    private UserGetDetailsService userGetDetailsService;

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUserById(@PathVariable int id){
        UserResponse user = userGetDetailsService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
