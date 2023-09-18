package com.dpdev.config;

import com.dpdev.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Set;

import static com.dpdev.entity.enums.Role.ADMIN;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests()
                .antMatchers("/login", "/logout", "/users", "/users/registration", "/users/{id}/avatar,",
                        "/products", "/products/{id}", "/v3/api-docs/**", "/swagger-ui/**")
                .permitAll()
                .antMatchers("/users/{\\d+}/delete").hasAnyAuthority(ADMIN.getAuthority())
                .antMatchers("/admin/**").hasAnyAuthority(ADMIN.getAuthority())
                .anyRequest().authenticated()
                .and()
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login"))
                .formLogin(login -> login
                        .loginPage("/login")
                        .defaultSuccessUrl("/users"))
                .oauth2Login(config -> config
                        .loginPage("/login")
                        .defaultSuccessUrl("/users", true)
                        .userInfoEndpoint(userInfo -> userInfo.oidcUserService(oidcUserService()))
                );
        return http.build();
    }

    private OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
        return userRequest -> {
            String email = userRequest.getIdToken().getEmail();
            UserDetails userDetails = userService.loadUserByUsername(email);
            DefaultOidcUser oidcUser = new DefaultOidcUser(userDetails.getAuthorities(), userRequest.getIdToken());

            Set<Method> userDetailsMethods = Set.of(UserDetails.class.getMethods());

            return (OidcUser) Proxy.newProxyInstance(SecurityConfiguration.class.getClassLoader(),
                    new Class[]{UserDetails.class, OidcUser.class},
                    (proxy, method, args) -> userDetailsMethods.contains(method)
                            ? method.invoke(userDetails, args)
                            : method.invoke(oidcUser, args));
        };
    }
}
