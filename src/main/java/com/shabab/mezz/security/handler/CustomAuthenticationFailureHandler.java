package com.shabab.mezz.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shabab.mezz.util.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.stereotype.Component;

/**
 * Project: Mezz-SpringBoot
 * Author: Shabab-1281539
 * Created on: 20/10/2024
 */

@Component("CustomAuthenticationFailureHandler")
public class CustomAuthenticationFailureHandler implements Customizer<ExceptionHandlingConfigurer<HttpSecurity>> {

    @Override
    public void customize(ExceptionHandlingConfigurer<HttpSecurity> httpSecurityExceptionHandlingConfigurer) {
        httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint((request, response, authException) -> {
            ApiResponse apiResponse;
            int status = HttpServletResponse.SC_UNAUTHORIZED;

            if (authException instanceof InsufficientAuthenticationException) {
                apiResponse = new ApiResponse(false, "Not Authorized");
            } else {
                apiResponse = new ApiResponse(false, authException.getMessage());
            }

            response.setStatus(status);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(apiResponse);
            response.getWriter().write(json);
        });
    }
}

