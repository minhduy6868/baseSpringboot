package com.example.demo.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.AbstractConfiguredSecurityBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final String[] PUBLIC_ENDPOINTS_POST = {
            "/user/create", "/auth/login", "auth/introspect", "user/createAdmin", "user/resetPassword"

    };
    @Value("${signAuthKey}")  //lấy giá trị signAuthKey từ file application.properties truyền vô bên dưới
    protected String SignIn_key;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        //cho phép truy cập khỏi cầu authentication
        httpSecurity.authorizeHttpRequests(request ->
                request.requestMatchers(HttpMethod.POST, "/user/create").permitAll()
                        .requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS_POST).permitAll()
                        .requestMatchers(HttpMethod.GET, "/user/getAllUsers").hasAuthority("SCOPE_ADMIN")
                        .anyRequest().authenticated()
        );

        //cấu hình Oauth2 với hỗ trợ JWT theo định dạng jwtDecoder ở dưới
        httpSecurity.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(
                        jwtDecoder()
                )));

        httpSecurity.csrf(AbstractHttpConfigurer::disable); //vì nó mặc định là chạy nên phải đóng nó
        return httpSecurity.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {  //Cung cấp cách giải mã và xác thực JWT bằng cách sử dụng khóa bí mật và thuật toán HMAC SHA-256.
        SecretKeySpec secretKeySpec = new SecretKeySpec(SignIn_key.getBytes(), "HmacSHA256");
        NimbusJwtDecoder nimbusJwtDecoder = NimbusJwtDecoder
                .withSecretKey(secretKeySpec)  //build key
                .macAlgorithm(MacAlgorithm.HS256) //build thuật toán
                .build();
        return  nimbusJwtDecoder;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

}