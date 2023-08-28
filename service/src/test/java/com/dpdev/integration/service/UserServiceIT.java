package com.dpdev.integration.service;

import com.dpdev.dto.UserCreateEditDto;
import com.dpdev.dto.filter.UserFilter;
import com.dpdev.dto.UserReadDto;
import com.dpdev.entity.enums.Role;
import com.dpdev.integration.IntegrationTestBase;
import com.dpdev.service.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@RequiredArgsConstructor
class UserServiceIT extends IntegrationTestBase {

    public static final Long USER_ID = 1L;
    public static final Long INVALID_USER_ID = -10L;

    private final UserService userService;

    @Test
    void findAll() {
        UserFilter filter = UserFilter.builder()
                .email("gmail")
                .build();
        Pageable pageable = PageRequest.of(0, 4);

        Page<UserReadDto> actualResult = userService.findAll(filter, pageable);

        assertThat(actualResult).isNotEmpty();
        assertThat(actualResult).hasSize(4);

        List<String> actualEmails = actualResult.stream()
                .map(obj -> obj.getEmail())
                .collect(Collectors.toList());

        assertThat(actualEmails).contains(
                "ivan@gmail.com",
                "sergey@gmail.com",
                "viktor@gmail.com",
                "andrey@gmail.com"
        );
    }

    @Test
    void findById() {
        Optional<UserReadDto> actualResult = userService.findById(USER_ID);

        assertThat(actualResult).isPresent();
        actualResult.ifPresent(obj -> {
            assertThat(obj.getId()).isEqualTo(USER_ID);
            assertThat(obj.getFirstname()).isEqualTo("Ivan");
            assertThat(obj.getLastname()).isEqualTo("Ivanov");
            assertThat(obj.getEmail()).isEqualTo("ivan@gmail.com");
            assertThat(obj.getPhoneNumber()).isEqualTo("1234567890");
            assertThat(obj.getAddress()).isEqualTo("BY, Minsk, 123 Sovetskaja St");
            assertSame(obj.getRole(), Role.USER);
        });
    }

    @Test
    void findByIdIfUserIsNotExist() {
        Optional<UserReadDto> actualResult = userService.findById(INVALID_USER_ID);

        assertThat(actualResult).isEmpty();
    }

    @Test
    void create() {
        UserCreateEditDto userCreateEditDto = UserCreateEditDto.builder()
                .firstname("User")
                .lastname("User")
                .email("user@gmail.com")
                .password("pass")
                .phoneNumber("511112116")
                .address("BY, Minsk, 300 Sovetskaja St")
                .role(Role.USER)
                .build();

        UserReadDto actualResult = userService.create(userCreateEditDto);

        assertThat(actualResult.getId()).isNotNull();
        Optional<UserReadDto> maybeUser = userService.findById(actualResult.getId());
        assertThat(maybeUser).isPresent();
        maybeUser.ifPresent(obj -> {
            assertThat(obj.getFirstname()).isEqualTo(userCreateEditDto.getFirstname());
            assertThat(obj.getLastname()).isEqualTo(userCreateEditDto.getLastname());
            assertThat(obj.getEmail()).isEqualTo(userCreateEditDto.getEmail());
            assertThat(obj.getPhoneNumber()).isEqualTo(userCreateEditDto.getPhoneNumber());
            assertThat(obj.getAddress()).isEqualTo(userCreateEditDto.getAddress());
            assertSame(obj.getRole(), userCreateEditDto.getRole());
        });
    }

    @Test
    void update() {
        UserCreateEditDto userCreateEditDto = UserCreateEditDto.builder()
                .firstname("User")
                .lastname("User")
                .email("user@gmail.com")
                .password("pass")
                .phoneNumber("511112116")
                .address("BY, Minsk, 300 Sovetskaja St")
                .role(Role.USER)
                .build();
        UserReadDto userReadDto = userService.create(userCreateEditDto);
        Long userId = userReadDto.getId();
        UserCreateEditDto updateUserDto = UserCreateEditDto.builder()
                .firstname("User1")
                .lastname("User1")
                .email("user1@gmail.com")
                .password("pass")
                .phoneNumber("511112116")
                .address("BY, Minsk, 300 Sovetskaja St")
                .role(Role.ADMIN)
                .build();

        Optional<UserReadDto> actualResult = userService.update(userId, updateUserDto);

        assertTrue(actualResult.isPresent());
        actualResult.ifPresent(obj -> {
            assertThat(obj.getFirstname()).isEqualTo(updateUserDto.getFirstname());
            assertThat(obj.getLastname()).isEqualTo(updateUserDto.getLastname());
            assertThat(obj.getEmail()).isEqualTo(updateUserDto.getEmail());
            assertThat(obj.getPhoneNumber()).isEqualTo(updateUserDto.getPhoneNumber());
            assertThat(obj.getAddress()).isEqualTo(updateUserDto.getAddress());
            assertSame(obj.getRole(), updateUserDto.getRole());
        });
    }

    @Test
    void updateIfUserIsNotExist() {
        UserCreateEditDto updateUserDto = UserCreateEditDto.builder()
                .firstname("User1")
                .lastname("User1")
                .email("user1@gmail.com")
                .password("pass")
                .phoneNumber("511112116")
                .address("BY, Minsk, 300 Sovetskaja St")
                .role(Role.ADMIN)
                .build();

        Optional<UserReadDto> maybeUser = userService.update(INVALID_USER_ID, updateUserDto);

        assertThat(maybeUser).isEmpty();
    }

    @Test
    void delete() {
        boolean actualResult = userService.delete(USER_ID);

        assertTrue(actualResult);
    }

    @Test
    void deleteIfUserIsNotExist() {
        Long userId = -1L;

        boolean actualResult = userService.delete(userId);

        assertFalse(actualResult);
    }
}
