package com.dpdev.dto;

import com.dpdev.entity.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserDtoTest {

    private UserDto userDto = new UserDto();


    @Test
    void getAllUsers() {
        userDto.createUser(new User());
        int expectedResult = 1;
        int actualResult = userDto.getAllUsers().size();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void createUser() {
        boolean actualResult = userDto.createUser(new User());
        assertTrue(actualResult);
    }
}