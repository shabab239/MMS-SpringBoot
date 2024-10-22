package com.shabab.mezz.security.service;

import com.shabab.mezz.core.dto.LoginDTO;
import com.shabab.mezz.security.jwt.JwtService;
import com.shabab.mezz.security.model.User;
import com.shabab.mezz.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Project: Mezz-SpringBoot
 * Author: Shabab-1281539
 * Created on: 20/10/2024
 */

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;


    public ApiResponse authenticate(LoginDTO loginDTO) {
        ApiResponse apiResponse = new ApiResponse();

        String cell = loginDTO.getCell();
        String password = loginDTO.getPassword();

        if (cell == null || cell.isEmpty()) {
            return apiResponse.error("Cell is required");
        }
        if (password == null || password.isEmpty()) {
            return apiResponse.error("Password is required");
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(cell, password)
            );
            if (authentication != null && authentication.isAuthenticated()) {
                User user = (User) authentication.getPrincipal();
                if (user != null) {
                    Map<String, Object> map = new HashMap<>();
                    String jwt = jwtService.generateJwt(user);
                    map.put("jwt", jwt);

                    apiResponse.setData(map);
                    apiResponse.setData("user", user);
                    apiResponse.success("User authenticated");
                    return apiResponse;
                }
            }
        } catch (Exception e) {
            apiResponse.setMessage("Invalid username or password");
        }
        return apiResponse;
    }
}


