package com.courseproject.eshop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Аида Есанян
 **/
@Getter
@Setter
@Entity
@Table(name = "carts")
public class CartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "cart", orphanRemoval = true)
    private UserEntity user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartItemEntity> cartItems = new LinkedHashSet<>();

    @Column(name = "total_cost")
    private BigDecimal totalCost = BigDecimal.valueOf(0);

    public void addItem(CartItemEntity item) {
        BigDecimal price = BigDecimal.valueOf(item.getProduct().getPrice()).multiply(BigDecimal.valueOf(item.getQuantity()));
        this.totalCost = price;
        this.cartItems.add(item);
    }

    public void removeItem(CartItemEntity item) {
        this.cartItems.remove(item);
        item.setCart(null);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
