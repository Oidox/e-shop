package com.courseproject.eshop.services;

import com.courseproject.eshop.entity.UserEntity;
import com.courseproject.eshop.enums.Role;
import com.courseproject.eshop.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * @author Аида Есанян
 **/
@Service
@Getter
@Setter
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    /**
     * Метод сохранения пользователя в БД
     */
    public void saveUser(UserEntity user) {
        userRepository.save(user);
    }

    /**
     * Метод установки и сохранения роли пользователя в БД
     */
    public void registerUser(UserEntity user) {
        user
                .setRole(Role.USER);
        userRepository.save(user);
    }

    /**
     * Метод получения пользователя по ID
     */
    public UserEntity getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    /**
     * Метод получения пользователя по email
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    /**
     * Метод для изменения данных пользователя, не требующих изменения всех полей
     */
    @Transactional
    public void updateUserProfile(UserEntity user, String newEmail, String newFirstName, String newLastName) {
        if (newEmail != null && !newEmail.isEmpty()) {
            user.setEmail(newEmail);
        }
        if (newFirstName != null && !newFirstName.isEmpty()) {
            user.setFirstName(newFirstName);
        }
        if (newLastName != null && !newLastName.isEmpty()) {
            user.setLastName(newLastName);
        }
        userRepository.save(user);
    }

    /**
     * Метод пополнения баланса пользователя
     */
    @Transactional
    public void addFunds(UserEntity user, BigDecimal amount) {
        user.setBalance(user.getBalance().add(amount));
        userRepository.save(user);
    }

}
