package com.dpdev.repository;

import com.dpdev.dto.filter.UserFilter;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.dpdev.entity.User;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.List;

import static com.dpdev.entity.QUser.user;

@RequiredArgsConstructor
public class FilterUserRepositoryImpl implements FilterUserRepository {

    private final EntityManager entityManager;

    @Override
    public List<User> findAllByFilter(UserFilter filter) {
        Predicate predicate = QPredicate.builder()
                .add(filter.getFirstname(), user.firstname::containsIgnoreCase)
                .add(filter.getLastname(), user.lastname::containsIgnoreCase)
                .add(filter.getUsername(), user.username::containsIgnoreCase)
                .buildAnd();
        return new JPAQuery<User>(entityManager)
                .select(user)
                .from(user)
                .where(predicate)
                .fetch();
    }
}
