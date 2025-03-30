package com.courseproject.eshop.controller;

import com.courseproject.eshop.dto.cart.CartDTO;
import com.courseproject.eshop.entity.CartEntity;
import com.courseproject.eshop.entity.CartItemEntity;
import com.courseproject.eshop.entity.UserEntity;
import com.courseproject.eshop.exceptions.CartNotFoundException;
import com.courseproject.eshop.exceptions.ProductNotFoundException;
import com.courseproject.eshop.mappers.CartItemMapper;
import com.courseproject.eshop.mappers.CartMapper;
import com.courseproject.eshop.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Аида Есанян
 **/
@Controller
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;
    private final CartItemMapper cartItemMapper;
    private final CartMapper cartMapper;

    /**
     * GET-метод создает форму корзины и отображает продукты в ней в отсортированном порядке по имени
     */
    @GetMapping
    public String viewCart(@AuthenticationPrincipal UserEntity user, Model model) {
        try {
            CartEntity cartForSort = cartService.getCart(user);
            Set<CartItemEntity> sortedCartItems = cartForSort.getCartItems().stream()
                    .sorted((item1, item2) -> item1.getProduct().getName().compareToIgnoreCase(item2.getProduct().getName()))
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            cartForSort.setCartItems(sortedCartItems);
            CartDTO cart = cartMapper.toDTO(cartForSort);
            double totalCost = cart.getCartItems().stream()
                    .map(cartItemMapper::cartItemDTOToCartItem)
                    .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                    .sum();
            model.addAttribute("cart", cart);
            model.addAttribute("totalCost", totalCost);
        } catch (CartNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return "cart";
    }

    /**
     * POST-метод добавляет продукт в корзину и перенаправляет на корзину
     */
    @PostMapping("/add")
    public String addItemToCart(@AuthenticationPrincipal UserEntity user,
                                @RequestParam String productName,
                                @RequestParam int quantity) {
        try {
            cartService.getCart(user);
            cartService.addItemToCart(user, productName, quantity);
        } catch (CartNotFoundException | ProductNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return "redirect:/cart";
    }

    /**
     * POST-метод удаляет продукт из корзины и перенаправляет на корзину
     */
    @PostMapping("/remove")
    public String removeItemFromCart(@AuthenticationPrincipal UserEntity user,
                                     @RequestParam Long itemId) {
        try {
            cartService.removeItemFromCart(user, itemId);
        } catch (CartNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return "redirect:/cart";
    }

    /**
     * POST-метод очищает корзину и перенаправляет на корзину
     */
    @PostMapping("/clear")
    public String clearCart(@AuthenticationPrincipal UserEntity user) {
        try {
            cartService.clearCart(user);
        } catch (CartNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return "redirect:/cart";
    }

    /**
     * POST-метод обрабатывает запрос на увелечения продукта в корзине на 1 и перенаправляет на корзину
     */
    @PostMapping("/incrementQuantity")
    public String incrementQuantity(@AuthenticationPrincipal UserEntity user,
                                    @RequestParam String productName) {
        try {
            cartService.incrementQuantity(user, productName);
        } catch (CartNotFoundException | ProductNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return "redirect:/cart";
    }

    /**
     * POST-метод обрабатывает запрос на уменьшение продукта в корзине на 1 и перенаправляет на корзину
     */
    @PostMapping("/decrementQuantity")
    public String decrementQuantity(@AuthenticationPrincipal UserEntity user,
                                    @RequestParam String productName) {
        try {
            cartService.decrementQuantity(user, productName);
        } catch (CartNotFoundException | ProductNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return "redirect:/cart";
    }

    /**
     * POST-метод формы оплаты. В случае возникновения ошибки, показывает её пользователю
     */
    @PostMapping("/purchase")
    public String purchase(@RequestParam double totalCost,
                           @AuthenticationPrincipal UserEntity user,
                           Model model) {
        try {
            cartService.purchase(totalCost, user);
            return "thankPage";
        } catch (Exception e) {
            CartEntity cartForSort = null;
            try {
                cartForSort = cartService.getCart(user);
            } catch (CartNotFoundException ex) {
                cartService.createCart(user);
                return "redirect:/cart";
            }
            CartDTO cart = cartMapper.toDTO(cartForSort);
            model.addAttribute("cart", cart);
            model.addAttribute("totalCost", totalCost);
            model.addAttribute("error", e.getMessage());
            return "cart";
        }
    }

    /**
     * GET-метод создает форму в случае успешной оплаты
     */
    @GetMapping("/thankPage")
    public String thankPage() {
        return "thankPage";
    }
}
