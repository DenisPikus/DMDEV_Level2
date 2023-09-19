package mapper;

import com.dpdev.dto.UserCreateEditDto;
import com.dpdev.entity.User;
import com.dpdev.entity.enums.Role;
import com.dpdev.mapper.UserCreateEditMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserCreateEditMapperTest {

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    MultipartFile mockFile;

    @InjectMocks
    private UserCreateEditMapper userCreateEditMapper;

    @Test
    public void map() {
        UserCreateEditDto dto = UserCreateEditDto.builder()
                .firstname("User")
                .lastname("User")
                .username("user@gmail.com")
                .rawPassword("pass")
                .phoneNumber("511112116")
                .address("BY, Minsk, 300 Sovetskaja St")
                .role(Role.USER)
                .image(mockMultipartFile())
                .build();
        User user = new User();
        when(passwordEncoder.encode("pass")).thenReturn("pass");

        userCreateEditMapper.map(dto, user);

        assertEquals(dto.getFirstname(), user.getFirstname());
        assertEquals(dto.getLastname(), user.getLastname());
        assertEquals(dto.getUsername(), user.getUsername());
        assertEquals(dto.getPhoneNumber(), user.getPhoneNumber());
        assertEquals(dto.getAddress(), user.getAddress());
        assertEquals(dto.getRole(), user.getRole());
        assertEquals(dto.getRawPassword(), user.getPassword());
        assertEquals(dto.getImage().getOriginalFilename(), user.getImage());
    }

    private MultipartFile mockMultipartFile() {
        when(mockFile.isEmpty()).thenReturn(false);
        when(mockFile.getOriginalFilename()).thenReturn("test.jpg");
        return mockFile;
    }
}
