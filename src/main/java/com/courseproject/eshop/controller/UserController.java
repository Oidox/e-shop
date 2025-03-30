package com.courseproject.eshop.controller;

import com.courseproject.eshop.dto.product.ProductDTO;
import com.courseproject.eshop.dto.user.UserDTO;
import com.courseproject.eshop.entity.UserEntity;
import com.courseproject.eshop.mappers.ProductMapper;
import com.courseproject.eshop.mappers.UserMapper;
import com.courseproject.eshop.services.ProductService;
import com.courseproject.eshop.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Аида Есанян
 **/
@Controller
@RequiredArgsConstructor
@RequestMapping("/home")
public class UserController {
    private final UserService userService;
    private final ProductService productService;
    private final ProductMapper productMapper;
    private final UserMapper userMapper;

    /**
     * GET-метод создает форму главной страницы с товарами и предоставляет доступ к работе с профилем и балансом
     */
    @GetMapping
    public String homePage(Model model) {
        List<ProductDTO> productDTOList = productService.getAllProduct().stream().map(productMapper::toDTO).toList();
        model.addAttribute("products", productDTOList);
        return "home";
    }

    /**
     * GET-метод создает форму профиля пользователя
     */
    @GetMapping("/profile")
    public String profilePage(@AuthenticationPrincipal UserEntity user, Model model) {
        UserDTO userDTO = userMapper.toDTO(userService.getUserById(user.getId()));
        model.addAttribute("user", userDTO);
        return "profile";
    }

    /**
     * GET-метод создает форму для редактирования профиля пользователя
     */
    @GetMapping("/profile/editProfile")
    public String editProfilePage(@AuthenticationPrincipal UserEntity user, Model model) {
        model.addAttribute("user", user);
        return "editProfile";
    }

    /**
     * POST-метод обрабатывает форму редактирования профиля пользователя и сохраняет изменения в БД и перенаправляет на страницу профиля пользователя
     */
    @PostMapping("/profile/update")
    public String updateProfile(@AuthenticationPrincipal UserEntity user,
                                @RequestParam(required = false) String newEmail,
                                @RequestParam(required = false) String newFirstName,
                                @RequestParam(required = false) String newLastName) {
        userService.updateUserProfile(user, newEmail, newFirstName, newLastName);
        return "redirect:/home/profile";
    }

    /**
     * POST-метод пополняет баланс пользователя и перенаправляет на страницу профиля пользователя
     */
    @PostMapping("/profile/addFunds")
    public String addFunds(@AuthenticationPrincipal UserEntity user, @RequestParam BigDecimal amount) {
        userService.addFunds(user, amount);
        return "redirect:/home/profile";
    }

}
