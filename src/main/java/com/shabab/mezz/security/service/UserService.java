package com.shabab.mezz.security.service;

import com.shabab.mezz.core.repository.MessRepository;
import com.shabab.mezz.security.model.Mess;
import com.shabab.mezz.security.model.User;
import com.shabab.mezz.security.repository.UserRepository;
import com.shabab.mezz.util.ApiResponse;
import com.shabab.mezz.util.AuthUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Project: Mezz-SpringBoot
 * Author: Shabab-1281539
 * Created on: 20/10/2024
 */

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessRepository messRepository;

    public ApiResponse getAllUsers() {
        ApiResponse response = new ApiResponse();
        try {
            List<User> users = userRepository.findAllByMess_Id(
                    AuthUtil.getCurrentMessId()
            ).orElse(new ArrayList<>());
            response.setData("users", users);
            response.success("Successfully retrieved users");
        } catch (Exception e) {
            return response.error(e);
        }
        return response;
    }

    @Transactional(rollbackOn = Exception.class)
    public ApiResponse saveUser(User user) {
        ApiResponse response = new ApiResponse();
        try {
            Mess mess = messRepository.findById(
                    AuthUtil.getCurrentMessId()
            ).orElse(null);
            if (mess == null) {
                return response.error("Mess not found");
            }
            user.setPassword(new BCryptPasswordEncoder(12).encode(user.getPassword()));
            user.setBalance(0.0);
            user.setMess(new Mess(mess.getId()));
            userRepository.save(user);
            response.setData("user", user);
            response.success("User created successfully.");
        } catch (Exception e) {
            return response.error(e);
        }
        return response;
    }

    @Transactional(rollbackOn = Exception.class)
    public ApiResponse updateUser(User updatedUser) {
        ApiResponse response = new ApiResponse();
        try {

            User existingUser = userRepository.findByIdAndMess_Id(
                    updatedUser.getId(), AuthUtil.getCurrentMessId()
            ).orElse(null);
            if (existingUser == null) {
                return response.error("User not found");
            }

            User currentUser = AuthUtil.getCurrentUser();
            if (updatedUser.getRole().equals(User.Role.ROLE_ADMIN) || updatedUser.getRole().equals(User.Role.ROLE_MANAGER)) {
                if (!currentUser.getRole().equals(User.Role.ROLE_ADMIN)) {
                    return response.error("You do not have permission to change user roles.");
                }
            }

            existingUser.setName(updatedUser.getName());
            existingUser.setCell(updatedUser.getCell());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setAvatar(updatedUser.getAvatar());
            existingUser.setRole(updatedUser.getRole());
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                existingUser.setPassword(new BCryptPasswordEncoder(12).encode(updatedUser.getPassword()));
            }

            userRepository.save(existingUser);
            response.setData("user", existingUser);
            response.success("User updated successfully.");
        } catch (Exception e) {
            return response.error("Error updating user: " + e.getMessage());
        }
        return response;
    }


    @Override
    public UserDetails loadUserByUsername(String cell) throws UsernameNotFoundException {
        User user = userRepository.findByCell(cell).orElse(null);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }
}