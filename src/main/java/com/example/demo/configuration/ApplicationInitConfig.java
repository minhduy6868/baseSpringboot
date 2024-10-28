package com.example.demo.configuration;

import com.example.demo.configuration.enums.Role;
import com.example.demo.entity.User;
import com.example.demo.reponsitory.UserReponsitory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Configuration
@Slf4j  // aninotation allow log something
public class ApplicationInitConfig {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserReponsitory userReponsitory) {
        return args -> {
            if(userReponsitory.findByUsername("admin").isEmpty()) {
                HashSet<String> roles = new HashSet<>();
                roles.add(Role.ADMIN.name());

                User user = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .roles(roles)
                        .build();

                userReponsitory.save(user);
                log.warn("admin user has been create, username: addmin - password: admin");
            }
        };
    }
}
