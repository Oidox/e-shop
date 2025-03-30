package com.courseproject.eshop.dto.product;

import com.courseproject.eshop.enums.ProductType;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Аида Есанян
 **/
@Getter
@Setter
public class ProductDTO {
    private String name;
    private double price;
    private int quantity;
    private ProductType productType;
}
