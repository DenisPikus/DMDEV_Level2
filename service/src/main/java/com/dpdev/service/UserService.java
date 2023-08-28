package com.dpdev.service;

import com.dpdev.dto.UserCreateEditDto;
import com.dpdev.dto.UserFilter;
import com.dpdev.dto.UserReadDto;
import com.dpdev.mapper.UserCreateEditMapper;
import com.dpdev.mapper.UserReadMapper;
import com.dpdev.repository.QPredicate;
import com.dpdev.repository.UserRepository;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.dpdev.entity.QUser.user;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository repository;
    private final UserReadMapper userReadMapper;
    private final UserCreateEditMapper userCreateEditMapper;

    public Page<UserReadDto> findAll(UserFilter filter, Pageable pageable) {
        Predicate predicate = QPredicate.builder()
                .add(filter.getFirstname(), user.firstname::containsIgnoreCase)
                .add(filter.getLastname(), user.lastname::containsIgnoreCase)
                .add(filter.getEmail(), user.email::containsIgnoreCase)
                .buildAnd();

        return repository.findAll(predicate, pageable)
                .map(userReadMapper::map);
    }

    public Optional<UserReadDto> findById(Long id) {
        return repository.findById(id)
                .map(userReadMapper::map);
    }

    @Transactional
    public UserReadDto create(UserCreateEditDto userDto) {
        return Optional.of(userDto)
                .map(userCreateEditMapper::map)
                .map(repository::save)
                .map(userReadMapper::map)
                .orElseThrow();
    }

    @Transactional
    public Optional<UserReadDto> update(Long id, UserCreateEditDto userDto) {
        return repository.findById(id)
                .map(user -> userCreateEditMapper.map(userDto, user))
                .map(repository::saveAndFlush)
                .map(userReadMapper::map);
    }

    @Transactional
    public boolean delete(Long id) {
        return repository.findById(id)
                .map(user -> {
                    repository.delete(user);
                    repository.flush();
                    return true;
                })
                .orElse(false);
    }
}
