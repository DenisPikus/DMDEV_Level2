package com.dpdev.mapper;

import com.dpdev.dto.UserCreateEditDto;
import com.dpdev.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.function.Predicate;

import static java.util.function.Predicate.*;

@Component
public class UserCreateEditMapper implements Mapper<UserCreateEditDto, User> {

    @Override
    public User map(UserCreateEditDto fromObject, User toObject) {
        copy(fromObject, toObject);
        return toObject;
    }

    @Override
    public User map(UserCreateEditDto object) {
        User user = new User();
        copy(object, user);
        return user;
    }

    private void copy(UserCreateEditDto object, User user) {
        user.setFirstname(object.getFirstname());
        user.setLastname(object.getLastname());
        user.setEmail(object.getEmail());
        user.setPassword(object.getPassword());
        user.setPhoneNumber(object.getPhoneNumber());
        user.setAddress(object.getAddress());
        user.setRole(object.getRole());

        Optional.ofNullable(object.getImage())
                .filter(not(MultipartFile::isEmpty))
                .ifPresent(image -> user.setImage(image.getOriginalFilename()));
    }
}
