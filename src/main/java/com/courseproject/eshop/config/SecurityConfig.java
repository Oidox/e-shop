package com.courseproject.eshop.config;

import com.courseproject.eshop.repository.UserRepository;
import com.courseproject.eshop.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author Аида Есанян
 **/
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserRepository repository;

    /**
     * Получение данных пользователя при аутентификации
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserService(repository);
    }

    /**
     * Кодирование пароля
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Метод определяет настройки безопасности для HTTP-запросов
     * <p>
     * URL-адреса, которые разрешены без аутентификации: home, /auth/register, /product/**
     * </p>
     * <p>
     * URL-адреса, которые требуют у пользователя роль ADMIN: /admin/** и /actuator/**
     * </p>
     */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("home", "/auth/register", "/product/**").permitAll()
                        .requestMatchers("/admin/**", "/actuator/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin((login) -> login
                        .permitAll()
                        .loginPage("/auth/login")
                        .defaultSuccessUrl("/home", true)
                );
        return http.build();
    }

    /**
     * Аутентификация пользователя из БД
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(this.userDetailsService());
        provider.setPasswordEncoder(this.passwordEncoder());
        return provider;
    }
}
