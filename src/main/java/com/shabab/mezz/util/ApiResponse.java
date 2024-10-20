package com.shabab.mezz.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Project: Mezz-SpringBoot
 * Author: Shabab-1281539
 * Created on: 20/10/2024
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {

    private String message = "";
    private Map<String, Object> data = new HashMap<>();
    private Map<String, String> errors = new HashMap<>();
    private boolean successful = false;

    public ApiResponse(boolean successful) {
        this.successful = successful;
    }

    public ApiResponse(boolean successful, String message) {
        this.successful = successful;
        this.message = message;
    }

    public ApiResponse setData(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

    public Object getData(String key) {
        return this.data.get(key);
    }

    public ApiResponse addError(String key, String errorMessage) {
        this.errors.put(key, errorMessage);
        return this;
    }

    public boolean hasErrors() {
        return !this.errors.isEmpty();
    }

    public ApiResponse success(String message) {
        this.successful = true;
        this.message = message;
        return this;
    }

    public ApiResponse error(String message) {
        this.successful = false;
        this.message = message;
        return this;
    }

    public ApiResponse error(Exception e) {
        this.successful = false;
        this.message = Optional.ofNullable(e.getCause())
                .map(Throwable::getMessage)
                .orElse(e.getMessage());
        return this;
    }
}
