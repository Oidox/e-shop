package com.courseproject.eshop.services;

import com.courseproject.eshop.entity.ProductEntity;
import com.courseproject.eshop.exceptions.ProductNotFoundException;
import com.courseproject.eshop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Аида Есанян
 **/
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    /**
     * Метод получения всех продуктов
     */
    public List<ProductEntity> getAllProduct() {
        return productRepository.findAll();
    }

    /**
     * Метод получения продукта по имени
     */
    public ProductEntity getProductByName(String name) throws ProductNotFoundException {
        return productRepository.findByName(name).orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }

    /**
     * Метод сохранения продукта
     */
    @Transactional
    public ProductEntity saveProduct(ProductEntity product) {
        return productRepository.save(product);
    }

    /**
     * Метод удаления продукта по имени
     */
    @Transactional
    public void deleteProductByName(String name) {
        productRepository.deleteProductByName(name);
    }

    /**
     * Метод редактирования продукта
     */
    @Transactional
    public void updateProduct(ProductEntity product, String newName, double newPrice, int newQuantity) {
        if (newName != null && !newName.isEmpty()) {
            product.setName(newName);
        }
        if (newPrice >= 0) {
            product.setPrice(newPrice);
        }
        if (newQuantity >= 0) {
            product.setQuantity(newQuantity);
        }
        productRepository.save(product);
    }
}
