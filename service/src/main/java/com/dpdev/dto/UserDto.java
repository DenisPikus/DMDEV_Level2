package com.dpdev.dto;

import com.dpdev.entity.User;

import java.util.ArrayList;
import java.util.List;

public class UserDto {

    private List<User> users = new ArrayList<>();
    public List<User> getAllUsers() {
        return users;
    }

    public boolean createUser(User user) {
        if (user != null) {
            users.add(user);
            return true;
        }
        return false;
    }
}
