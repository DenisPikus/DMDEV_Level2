package com.dpdev.service;

import com.dpdev.dto.UserDto;
import com.dpdev.entity.User;

import java.util.List;

public class UserService {

    private UserDto userDto = new UserDto();

    public List<UserDto> getAllUsers(){
        List<User> allUsers = userDto.getAllUsers();
        return userDto.getAllUsers()
                .stream()
                .map(it -> new UserDto())
                .toList();
    }

}
