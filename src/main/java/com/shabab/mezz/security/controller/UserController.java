package com.shabab.mezz.security.controller;

import com.shabab.mezz.security.model.User;
import com.shabab.mezz.security.service.UserService;
import com.shabab.mezz.util.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Project: Mezz-SpringBoot
 * Author: Shabab-1281539
 * Created on: 20/10/2024
 */

@CrossOrigin
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public ApiResponse getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/save")
    public ApiResponse saveUser(@Valid @RequestBody User user) {
        return userService.saveUser(user);
    }

    @PostMapping("/update")
    public ApiResponse updateUser(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }

}
