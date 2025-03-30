package com.courseproject.eshop.services;

import com.courseproject.eshop.entity.CartItemEntity;
import com.courseproject.eshop.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Аида Есанян
 **/
@Service
@RequiredArgsConstructor
public class CartItemService {
    private final CartItemRepository repository;

    /**
     * Метод добавления продута в корзину пользователя
     */
    @Transactional
    public void saveCartItem(CartItemEntity cartItem){
        repository.save(cartItem);
    }

    /**
     * Метод удаления продута из корзины пользователя
     */
    @Transactional
    public void deleteCartItem(CartItemEntity cartItem){
        repository.delete(cartItem);
    }
}
