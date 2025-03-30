package com.courseproject.eshop.entity;

import com.courseproject.eshop.enums.ProductType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Аида Есанян
 **/
@Getter
@Setter
@Entity
@Table(name = "products")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private int quantity = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_type", nullable = false)
    private ProductType productType = ProductType.GAME;

}
