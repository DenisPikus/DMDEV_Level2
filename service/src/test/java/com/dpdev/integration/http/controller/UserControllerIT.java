package com.dpdev.integration.http.controller;

import com.dpdev.dto.UserCreateEditDto;
import com.dpdev.entity.enums.Role;
import com.dpdev.integration.IntegrationTestBase;
import com.dpdev.service.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static com.dpdev.dto.UserCreateEditDto.Fields.address;
import static com.dpdev.dto.UserCreateEditDto.Fields.email;
import static com.dpdev.dto.UserCreateEditDto.Fields.firstname;
import static com.dpdev.dto.UserCreateEditDto.Fields.lastname;
import static com.dpdev.dto.UserCreateEditDto.Fields.password;
import static com.dpdev.dto.UserCreateEditDto.Fields.phoneNumber;
import static com.dpdev.dto.UserCreateEditDto.Fields.role;
import static org.mockito.Mockito.when;
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

    public static final Long USER_ID = 1L;
    public static final Long INVALID_USER_ID = -1L;

    private final MockMvc mockMvc;
    private final UserService userService;

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
                .andExpectAll(
                        status().isOk(),
                        model().attributeExists("user"),
                        model().attributeExists("roles"),
                        view().name("user/user")
                );
    }

    @Test
    public void findByIdWhenUserIsNotPresent() throws Exception {
        mockMvc.perform(get("/users/{id}", INVALID_USER_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void create() throws Exception {
        mockMvc.perform(post("/users")
                        .param(firstname, "Test")
                        .param(lastname, "Test")
                        .param(email, "test@mail.com")
                        .param(password, "pas")
                        .param(phoneNumber, "555555")
                        .param(address, "By, Minsk")
                        .param(role, "ADMIN")
                )
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrlPattern("/users/{\\d+}")
                );
    }

    @Test
    void update() throws Exception {
        UserCreateEditDto updateUserDto = UserCreateEditDto.builder()
                .firstname("User1")
                .lastname("User1")
                .email("user1@gmail.com")
                .password("pass")
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

        //when(userService.delete(USER_ID)).thenReturn(true);

        mockMvc.perform(post("/users/{id}/delete", USER_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));
    }

    @Test
    public void testDeleteUser_NotFound() throws Exception {
       // when(userService.delete(INVALID_USER_ID)).thenReturn(false);

        mockMvc.perform(post("/users/{id}/delete", INVALID_USER_ID))
                .andExpect(status().isNotFound());
    }
}