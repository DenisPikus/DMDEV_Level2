package com.dpdev.integration.config;

import com.dpdev.config.ApplicationConfiguration;
import com.dpdev.integration.util.HibernateTestUtil;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(ApplicationConfiguration.class)
public class ApplicationTestConfiguration {

    @Bean
    public SessionFactory sessionFactory() {
        return HibernateTestUtil.buildSessionFactory();
    }
}
