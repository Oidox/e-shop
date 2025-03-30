package com.courseproject.eshop.services;

import com.courseproject.eshop.entity.CartEntity;
import com.courseproject.eshop.entity.CartItemEntity;
import com.courseproject.eshop.entity.ProductEntity;
import com.courseproject.eshop.entity.UserEntity;
import com.courseproject.eshop.exceptions.AppBalanceException;
import com.courseproject.eshop.exceptions.CartNotFoundException;
import com.courseproject.eshop.exceptions.ProductNotFoundException;
import com.courseproject.eshop.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * @author Аида Есанян
 **/
@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final ProductService productService;
    private final CartItemService cartItemService;
    private final UserService userService;

    /**
     * Метод получения корзины из БД по пользователю
     */
    @Transactional
    public CartEntity getCart(UserEntity user) throws CartNotFoundException {
        return cartRepository.findByUser(user).orElseThrow(
                () -> new CartNotFoundException("Cart no found"));
    }

    /**
     * Метод создания корзины пользователю
     */
    @Transactional
    public void createCart(UserEntity user) {
        CartEntity newCart = new CartEntity();
        newCart.setUser(user);
        user.setCart(newCart);
        cartRepository.save(newCart);
    }

    /**
     * Метод добавления продукта в корзину
     */
    @Transactional
    public void addItemToCart(UserEntity user, String productName, int quantity) throws CartNotFoundException, ProductNotFoundException {
        CartEntity cart = cartRepository.findByUser(user).orElseThrow(() -> new CartNotFoundException("Cart not found"));
        ProductEntity product = productService.getProductByName(productName);

        Optional<CartItemEntity> existingItemOpt = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getName().equals(productName))
                .findFirst();

        if (existingItemOpt.isPresent()) {
            CartItemEntity existingItem = existingItemOpt.get();
            // Проверка на случай если кол-во предметов в корзине превышает допустимое количество
            // Если такое произошло - ставит в поле quantity максимальное количество продукта в наличии
            if (existingItem.getProduct().getQuantity() > existingItem.getQuantity() + quantity) {
                existingItem.setQuantity(existingItem.getQuantity() + quantity);
                cartItemService.saveCartItem(existingItem);
            } else {
                existingItem.setQuantity(existingItem.getProduct().getQuantity());
                cartItemService.saveCartItem(existingItem);
            }
        } else {
            CartItemEntity cartItem = new CartItemEntity();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cart.addItem(cartItem);
            cartItemService.saveCartItem(cartItem);
        }
    }

    /**
     * Метод удаления продукта из корзины
     */
    @Transactional
    public void removeItemFromCart(UserEntity user, Long itemId) throws CartNotFoundException {
        CartEntity cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new CartNotFoundException("Cart not Found"));
        if (cart != null) {
            Optional<CartItemEntity> cartItemOpt = cart.getCartItems().stream()
                    .filter(item -> item.getId().equals(itemId))
                    .findFirst();
            if (cartItemOpt.isPresent()) {
                CartItemEntity cartItem = cartItemOpt.get();
                cart.removeItem(cartItem);
                cartItemService.deleteCartItem(cartItem);
            }
        }
    }

    /**
     * Метод очистки всех продуктов из корзины
     */
    @Transactional
    public void clearCart(UserEntity user) throws CartNotFoundException {
        CartEntity cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new CartNotFoundException("Cart not Found"));
        if (cart != null) {
            cart.getCartItems().clear();
            cartRepository.save(cart);
        }
    }

    /**
     * Метод для работы счетчика количества продукта внутри корзины (увеличение)
     */
    @Transactional
    public void incrementQuantity(UserEntity user, String productName) throws CartNotFoundException, ProductNotFoundException {
        CartEntity cart = cartRepository.findByUser(user).orElseThrow(() -> new CartNotFoundException("Cart not found"));
        ProductEntity product = productService.getProductByName(productName);

        Optional<CartItemEntity> cartItemOpt = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getName().equals(productName))
                .findFirst();

        if (cartItemOpt.isPresent()) {
            CartItemEntity cartItem = cartItemOpt.get();
            if (cartItem.getQuantity() < product.getQuantity()) {
                cartItem.setQuantity(cartItem.getQuantity() + 1);
                cartItemService.saveCartItem(cartItem);
            }
        }
    }

    /**
     * Метод для работы счетчика количества продукта внутри корзины (уменьшение)
     */
    @Transactional
    public void decrementQuantity(UserEntity user, String productName) throws CartNotFoundException, ProductNotFoundException {
        CartEntity cart = cartRepository.findByUser(user).orElseThrow(() -> new CartNotFoundException("Cart not found"));

        Optional<CartItemEntity> cartItemOpt = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getName().equals(productName))
                .findFirst();

        if (cartItemOpt.isPresent()) {
            CartItemEntity cartItem = cartItemOpt.get();
            if (cartItem.getQuantity() > 1) {
                cartItem.setQuantity(cartItem.getQuantity() - 1);
                cartItemService.saveCartItem(cartItem);
            } else {
                removeItemFromCart(user, cartItem.getId());
            }
        }
    }

    /**
     * Метод обновления баланса и изменения количества товара после покупки
     */
    @Transactional
    public void purchase(double totalCost, UserEntity user) throws CartNotFoundException, AppBalanceException {
        // Загружаем пользователя заново, чтобы гарантировать активную сессию
        UserEntity persistentUser = userService.getUserById(user.getId());

        // Инициализируем ленивую коллекцию корзины
        CartEntity cart = persistentUser.getCart();
        cart.getCartItems().size();

        // Проверяем баланс пользователя
        if (persistentUser.getBalance().compareTo(BigDecimal.valueOf(totalCost)) >= 0) {
            for (CartItemEntity cartItem : cart.getCartItems()) {
                ProductEntity product = cartItem.getProduct();
                int remainingQuantity = product.getQuantity() - cartItem.getQuantity();
                if (remainingQuantity < 0) {
                    throw new AppBalanceException("Insufficient stock for product: " + product.getName());
                }
                product.setQuantity(remainingQuantity);
                productService.saveProduct(product);
            }
            persistentUser.setBalance(persistentUser.getBalance().subtract(BigDecimal.valueOf(totalCost)));
            clearCart(persistentUser);
            userService.saveUser(persistentUser);
        } else {
            throw new AppBalanceException("Insufficient balance");
        }
    }
}
