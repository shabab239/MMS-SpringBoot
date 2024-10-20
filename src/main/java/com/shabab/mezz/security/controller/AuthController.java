package com.shabab.mezz.security.controller;

import com.shabab.mezz.security.service.AuthService;
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
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ApiResponse login(
            @RequestParam(required = false) String cell,
            @RequestParam(required = false) String password
    ) {
        return authService.authenticate(cell, password);
    }

}
