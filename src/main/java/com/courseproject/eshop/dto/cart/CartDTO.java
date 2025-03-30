package com.courseproject.eshop.dto.cart;

import com.courseproject.eshop.dto.carItem.CartItemDTO;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

/**
 * @author Аида Есанян
 **/
@Getter
@Setter
public class CartDTO {
    private Long id;
    private BigDecimal totalCost = BigDecimal.valueOf(0);
    private Set<CartItemDTO> cartItems;
}
