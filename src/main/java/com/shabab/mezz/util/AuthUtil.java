package com.shabab.mezz.util;

import com.shabab.mezz.security.model.Mess;
import com.shabab.mezz.security.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Project: Mezz-SpringBoot
 * Author: Shabab-1281539
 * Created on: 20/10/2024
 */

public class AuthUtil {

    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static Long getCurrentUserId() {
        return getCurrentUser().getId();
    }

    public static Mess getCurrentMess() {
        return getCurrentUser().getMess();
    }

    public static Long getCurrentMessId() {
        return getCurrentMess().getId();
    }

}
