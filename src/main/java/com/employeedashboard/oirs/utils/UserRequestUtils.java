package com.employeedashboard.oirs.utils;

import com.employeedashboard.oirs.model.Employee;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

import static com.employeedashboard.oirs.model.Permission.ADMIN;

public class UserRequestUtils {
    static public UUID getCurrentUserID() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if(principal instanceof Employee) {
            return ((Employee) principal).getId();
        }
        return UUID.fromString("");
    }

    static public boolean checkIfCurrentUserHasAdminPermission() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().contains(new SimpleGrantedAuthority(ADMIN.toString()));
    }
}
