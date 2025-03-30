package com.courseproject.eshop.mappers;

import com.courseproject.eshop.dto.carItem.CartItemDTO;
import com.courseproject.eshop.entity.CartItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * @author Аида Есанян
 **/
@Mapper(componentModel = "spring")
public interface CartItemMapper {
    @Named("getTotalPrice")
    static double getTotalPrice(CartItemEntity cartItem) {
        return cartItem.getTotalPrice();
    }

    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.price", target = "price")
    @Mapping(source = "cartItem", target = "totalPrice", qualifiedByName = "getTotalPrice")
    CartItemDTO cartItemToCartItemDTO(CartItemEntity cartItem);

    @Mapping(source = "productName", target = "product.name")
    @Mapping(source = "price", target = "product.price")
    CartItemEntity cartItemDTOToCartItem(CartItemDTO cartItemDTO);
}
