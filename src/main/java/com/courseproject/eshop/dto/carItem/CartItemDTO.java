package com.courseproject.eshop.dto.carItem;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Аида Есанян
 **/
@Getter
@Setter
public class CartItemDTO {
    private Long id;
    private String productName;
    private int quantity;
    private double price;
    private double totalPrice;
}
