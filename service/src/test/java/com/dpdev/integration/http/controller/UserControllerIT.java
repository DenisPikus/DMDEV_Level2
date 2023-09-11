package com.dpdev.integration.http.controller;

import com.dpdev.dto.UserCreateEditDto;
import com.dpdev.entity.enums.Role;
import com.dpdev.integration.IntegrationTestBase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static com.dpdev.dto.UserCreateEditDto.Fields.address;
import static com.dpdev.dto.UserCreateEditDto.Fields.firstname;
import static com.dpdev.dto.UserCreateEditDto.Fields.lastname;
import static com.dpdev.dto.UserCreateEditDto.Fields.phoneNumber;
import static com.dpdev.dto.UserCreateEditDto.Fields.rawPassword;
import static com.dpdev.dto.UserCreateEditDto.Fields.role;
import static com.dpdev.dto.UserCreateEditDto.Fields.username;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@AutoConfigureMockMvc
@RequiredArgsConstructor
class UserControllerIT extends IntegrationTestBase {

    private static final Long USER_ID = 1L;
    private static final Long INVALID_USER_ID = -1L;

    private final MockMvc mockMvc;

    @Test
    void registration() throws Exception {
        mockMvc.perform(get("/users/registration"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/registration"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("roles"));
    }

    @Test
    void findAll() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpectAll(
                        status().isOk(),
                        model().attributeExists("users"),
                        view().name("user/users")
                );
    }

    @Test
    void findById() throws Exception {
        mockMvc.perform(get("/users/{id}", USER_ID))
                .andExpectAll(status().isOk(),
                        model().attributeExists("user"),
                        model().attributeExists("roles"),
                        view().name("user/user")
                );
    }

    @Test
    public void findByIdWhenUserIsNotPresent() throws Exception {
        mockMvc.perform(get("/users/{id}", INVALID_USER_ID))
                .andExpect(status().isNotFound()
                );
    }

    @Test
    void create() throws Exception {
        mockMvc.perform(post("/users")
                        .param(firstname, "Test")
                        .param(lastname, "Test")
                        .param(username, "test@mail.com")
                        .param(rawPassword, "pas").param(phoneNumber, "555555555555")
                        .param(address, "By, Minsk, Pobedy 67/152").param(role, "ADMIN"))
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrlPattern("/login")
                );
    }

    @Test
    void createThrowException() throws Exception {
        mockMvc.perform(post("/users")
                        .param(firstname, "Test")
                        .param(lastname, "Test")
                        .param(username, "badEmail")
                        .param(rawPassword, "pas").param(phoneNumber, "555555")
                        .param(address, "By, Minsk").param(role, "ADMIN"))
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrlPattern("/registration")
                );
    }

    @Test
    void update() throws Exception {
        UserCreateEditDto updateUserDto = UserCreateEditDto.builder()
                .firstname("User1")
                .lastname("User1")
                .username("user1@gmail.com")
                .rawPassword("pass")
                .phoneNumber("511112116")
                .address("BY, Minsk, 300 Sovetskaja St")
                .role(Role.ADMIN)
                .build();

        mockMvc.perform(post("/users/{id}/update", USER_ID)
                        .flashAttr("user", updateUserDto))
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrlPattern("/users/{\\d+}")
                );
    }

    @Test
    public void testDeleteUser() throws Exception {
        mockMvc.perform(post("/users/{id}/delete", USER_ID)
                        .with(user("admin@mail.com").authorities(Role.ADMIN)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users")
                );
    }

    @Test
    public void testDeleteUser_NotFound() throws Exception {
        mockMvc.perform(post("/users/{id}/delete", INVALID_USER_ID)
                        .with(user("user@mail.com").authorities(Role.ADMIN)))
                .andExpect(status().isNotFound());
    }
}