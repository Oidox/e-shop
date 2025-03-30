package com.courseproject.eshop.controller;

import com.courseproject.eshop.entity.UserEntity;
import com.courseproject.eshop.services.CartService;
import com.courseproject.eshop.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Аида Есанян
 **/
@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final UserService userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartService cartService;

    /**
     * Метод создает форму авторизации
     */
    @RequestMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    /**
     * GET-метод создает форму для регистрации пользователя
     */
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new UserEntity());
        return "register";
    }

    /**
     * POST-метод обрабатывает форму регистрации и сохраняет нового пользователя в БД
     */
    @PostMapping("/register")
    public String registerUser(@ModelAttribute UserEntity user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.registerUser(user);
        cartService.createCart(user);
        return "redirect:/login";
    }
}
