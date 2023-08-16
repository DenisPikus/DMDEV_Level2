package com.dpdev.integration.dao;

import com.dpdev.repository.UserRepository;
import com.dpdev.dto.UserWithAvgPriceProjection;
import com.dpdev.integration.IntegrationTestBase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
class UserRepositoryIT extends IntegrationTestBase {

    private final UserRepository userRepository;

    @Test
    void findUsersWithAvgOrdersOrderedByUsername() {
        List<UserWithAvgPriceProjection> results = userRepository.findUsersWithAvgOrdersOrderedByEmail();

        assertThat(results).hasSize(2);

        List<String> userEmails = results.stream()
                .map(obj -> obj.getEmail()).collect(toList());
        assertThat(userEmails).contains("ivan@gmail.com", "sergey@gmail.com");

        List<Double> avgOrders = results.stream()
                .map(obj -> obj.getAvgPrice()).collect(toList());
        assertThat(avgOrders).contains(8.66,
                8.99);
    }
}
