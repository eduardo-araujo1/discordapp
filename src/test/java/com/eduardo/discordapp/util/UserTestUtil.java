package com.eduardo.discordapp.util;

import com.eduardo.discordapp.enums.UserRole;
import com.eduardo.discordapp.model.User;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserTestUtil {

    public static UUID USER_ID = UUID.randomUUID();
    public static String USERNAME = "TestUser";
    public static String PASSWORD = "password123";
    public static String EMAIL = "testuser@example.com";
    public static UserRole ROLE = UserRole.USER;

    public static User createUser() {
        User user = new User();
        user.setUserId(USER_ID);
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);
        user.setEmail(EMAIL);
        user.setRole(ROLE);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return user;
    }
}
