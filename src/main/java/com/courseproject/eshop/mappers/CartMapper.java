package com.courseproject.eshop.mappers;

import com.courseproject.eshop.dto.cart.CartDTO;
import com.courseproject.eshop.entity.CartEntity;
import org.mapstruct.Mapper;

/**
 * @author Аида Есанян
 **/
@Mapper(componentModel = "spring", uses = CartItemMapper.class)
public interface CartMapper {
    CartDTO toDTO(CartEntity cart);

    CartEntity toCart(CartDTO cartDTO);
}
