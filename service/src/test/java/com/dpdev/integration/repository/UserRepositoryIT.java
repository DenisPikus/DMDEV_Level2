package com.dpdev.integration.repository;

import com.dpdev.dto.UserWithAvgPriceProjection;
import com.dpdev.integration.IntegrationTestBase;
import com.dpdev.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
class UserRepositoryIT extends IntegrationTestBase {

    private final UserRepository userRepository;

    @Test
    void findUsersWithAvgOrdersOrderedByUsername() {
        List<UserWithAvgPriceProjection> results = userRepository.findUsersWithAvgOrdersOrderedByEmail();

        assertThat(results).hasSize(5);

        List<BigDecimal> avgOrders = results.stream()
                .map(obj -> BigDecimal.valueOf(obj.getAvgPrice())
                        .setScale(2, RoundingMode.HALF_UP))
                .collect(toList());

        assertThat(avgOrders).contains(
                BigDecimal.valueOf(123.33),
                BigDecimal.valueOf(139.00).setScale(2),
                BigDecimal.valueOf(46.67),
                BigDecimal.valueOf(850.00).setScale(2),
                BigDecimal.valueOf(186.67)
        );

        List<String> userEmails = results.stream()
                .map(obj -> obj.getEmail()).collect(toList());
        assertThat(userEmails).contains(
                "ivan@gmail.com",
                "sergey@gmail.com",
                "viktor@gmail.com",
                "andrey@gmail.com",
                "sveta@gmail.com"
        );
    }
}
