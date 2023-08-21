package com.dpdev.repository;

import com.dpdev.dto.UserFilter;
import com.dpdev.entity.User;

import java.util.List;

public interface FilterUserRepository {

    List<User> findAllByFilter(UserFilter filter);
}
