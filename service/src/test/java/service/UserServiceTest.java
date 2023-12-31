package service;

import com.dpdev.dto.UserCreateEditDto;
import com.dpdev.dto.UserReadDto;
import com.dpdev.dto.filter.UserFilter;
import com.dpdev.entity.User;
import com.dpdev.entity.enums.Role;
import com.dpdev.mapper.UserCreateEditMapper;
import com.dpdev.mapper.UserReadMapper;
import com.dpdev.repository.UserRepository;
import com.dpdev.service.ImageService;
import com.dpdev.service.UserService;
import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserReadMapper userReadMapper;
    @Mock
    private UserCreateEditMapper userCreateEditMapper;
    @Mock
    ImageService imageService;
    @InjectMocks
    private UserService userService;

    @Test
    void findAll() {
        User user1 = User.builder()
                .firstname("User1")
                .lastname("User1")
                .username("user1@gmail.com")
                .password("pass")
                .phoneNumber("511112111")
                .address("BY, Minsk, 300 Sovetskaja St")
                .role(Role.USER)
                .build();
        User user2 = User.builder()
                .firstname("User2")
                .lastname("User2")
                .username("user2@gmail.com")
                .password("pass")
                .phoneNumber("511112115")
                .address("BY, Minsk, 300 Sovetskaja St")
                .role(Role.ADMIN)
                .build();
        UserReadDto user1ReadDto = UserReadDto.builder()
                .firstname("User1")
                .lastname("User1")
                .username("user1@gmail.com")
                .phoneNumber("511112111")
                .address("BY, Minsk, 300 Sovetskaja St")
                .role(Role.USER)
                .build();
        UserReadDto user2ReadDto = UserReadDto.builder()
                .firstname("User2")
                .lastname("User2")
                .username("user2@gmail.com")
                .phoneNumber("511112115")
                .address("BY, Minsk, 300 Sovetskaja St")
                .role(Role.ADMIN)
                .build();

        List<User> userList = Arrays.asList(user1, user2);
        UserFilter filter = UserFilter.builder()
                .firstname("User1")
                .build();
        Pageable pageable = PageRequest.of(0, 10);
        when(userReadMapper.map(user1)).thenReturn(user1ReadDto);
        when(userReadMapper.map(user2)).thenReturn(user2ReadDto);
        when(userRepository.findAll(any(Predicate.class), eq(pageable)))
                .thenReturn(new PageImpl<>(userList));

        Page<UserReadDto> actualResult = userService.findAll(filter, pageable);

        assertEquals(2, actualResult.getTotalElements());
        assertEquals("User1", actualResult.getContent().get(0).getLastname());
        verify(userRepository, times(1)).findAll(any(Predicate.class), eq(pageable));
    }

    @Test
    void findById() {
        Long userId = 1L;
        User user = User.builder()
                .id(userId)
                .firstname("User1")
                .lastname("User1")
                .username("user1@gmail.com")
                .password("pass")
                .phoneNumber("511112111")
                .address("BY, Minsk, 300 Sovetskaja St")
                .role(Role.USER)
                .build();
        UserReadDto expectedUserReadDto = UserReadDto.builder()
                .id(userId)
                .firstname("User1")
                .lastname("User1")
                .username("user1@gmail.com")
                .phoneNumber("511112111")
                .address("BY, Minsk, 300 Sovetskaja St")
                .role(Role.USER)
                .build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userReadMapper.map(user)).thenReturn(expectedUserReadDto);

        Optional<UserReadDto> actualResult = userService.findById(userId);

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get()).isEqualTo(expectedUserReadDto);
        verify(userRepository, times(1)).findById(user.getId());
    }

    @Test
    void create() {
        Long userId = 1L;
        UserCreateEditDto userCreateEditDto = UserCreateEditDto.builder()
                .firstname("User")
                .lastname("User")
                .username("user@gmail.com")
                .rawPassword("pass")
                .phoneNumber("511112116")
                .address("BY, Minsk, 300 Sovetskaja St")
                .role(Role.USER)
                .build();
        User user = User.builder()
                .firstname("User")
                .lastname("User")
                .username("user@gmail.com")
                .password("pass")
                .phoneNumber("511112116")
                .address("BY, Minsk, 300 Sovetskaja St")
                .role(Role.USER)
                .build();
        User savedUser = User.builder()
                .id(userId)
                .firstname("User")
                .lastname("User")
                .username("user@gmail.com")
                .password("pass")
                .phoneNumber("511112116")
                .address("BY, Minsk, 300 Sovetskaja St")
                .role(Role.USER)
                .build();

        doNothing().when(imageService).upload(anyString(), any());
        doReturn(user).when(userCreateEditMapper).map(userCreateEditDto);
        doReturn(savedUser).when(userRepository).save(user);

        UserReadDto expectedResult = UserReadDto.builder()
                .id(userId)
                .firstname("User")
                .lastname("User")
                .username("user@gmail.com")
                .phoneNumber("511112116")
                .address("BY, Minsk, 300 Sovetskaja St")
                .role(Role.USER)
                .build();
        when(userReadMapper.map(savedUser)).thenReturn(expectedResult);

        UserReadDto actualResult = userService.create(userCreateEditDto);

        assertThat(actualResult).isEqualTo(expectedResult);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void update() {
        Long userId = 1L;
        UserCreateEditDto userCreateEditDto = UserCreateEditDto.builder()
                .firstname("User1")
                .lastname("User1")
                .username("user1@gmail.com")
                .rawPassword("pass")
                .phoneNumber("511112116")
                .address("BY, Minsk, 300 Sovetskaja St")
                .role(Role.ADMIN)
                .build();
        User user = User.builder()
                .id(userId)
                .firstname("User")
                .lastname("User")
                .username("user@gmail.com")
                .password("pass")
                .phoneNumber("511112116")
                .address("BY, Minsk, 300 Sovetskaja St")
                .role(Role.USER)
                .build();
        User updatedUser = User.builder()
                .id(userId)
                .firstname("User1")
                .lastname("User1")
                .username("1@gmail.com")
                .password("pass")
                .phoneNumber("511112116")
                .address("BY, Minsk, 300 Sovetskaja St")
                .role(Role.ADMIN)
                .build();

        doNothing().when(imageService).upload(anyString(), any());
        doReturn(Optional.of(user)).when(userRepository).findById(userId);
        doReturn(updatedUser).when(userCreateEditMapper).map(userCreateEditDto, user);
        doReturn(updatedUser).when(userRepository).saveAndFlush(updatedUser);
        UserReadDto expectedResult = UserReadDto.builder()
                .id(userId)
                .firstname("User1")
                .lastname("User1")
                .username("1@gmail.com")
                .phoneNumber("511112116")
                .address("BY, Minsk, 300 Sovetskaja St")
                .role(Role.ADMIN)
                .build();
        when(userReadMapper.map(updatedUser)).thenReturn(expectedResult);

        Optional<UserReadDto> actualResult = userService.update(userId, userCreateEditDto);

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get()).isEqualTo(expectedResult);
        verify(userRepository, times(1)).saveAndFlush(updatedUser);
    }

    @Test
    void delete() {
        Long userId = 1L;
        User user = User.builder()
                .id(userId)
                .firstname("User")
                .lastname("User")
                .username("user@gmail.com")
                .password("pass")
                .phoneNumber("511112116")
                .address("BY, Minsk, 300 Sovetskaja St")
                .role(Role.USER)
                .build();
        doReturn(Optional.of(user)).when(userRepository).findById(userId);

        boolean expectedResult = userService.delete(userId);

        assertTrue(expectedResult);
        verify(userRepository).findById(userId);
        verify(userRepository).delete(user);
        verify(userRepository).flush();
    }

    @Test
    void deleteWhenUserIsNotPresent() {
        Long userId = 1L;
        doReturn(Optional.empty()).when(userRepository).findById(userId);

        boolean expectedResult = userService.delete(userId);

        assertFalse(expectedResult);
        verify(userRepository).findById(userId);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void findAvatar() {

    }

    @Test
    void findByUsername() {

    }

    @Test
    void loadUserByUsername() {

    }
}
