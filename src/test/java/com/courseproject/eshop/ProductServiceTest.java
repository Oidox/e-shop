package com.courseproject.eshop;

import com.courseproject.eshop.entity.ProductEntity;
import com.courseproject.eshop.repository.ProductRepository;
import com.courseproject.eshop.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * @author Аида Есанян
 **/
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepo;

    @InjectMocks
    private ProductService productService;

    private ProductEntity product;

    @BeforeEach
    void setUp() {
        product = new ProductEntity();
        product.setId(1L);
        product.setName("Test Product");
        product.setPrice(100.0);
        product.setQuantity(10);
    }

    @Test
    void testSaveProduct() {
        when(productRepo.save(product)).thenReturn(product);

        ProductEntity savedProduct = productService.saveProduct(product);

        assertNotNull(savedProduct);
        assertEquals(product.getId(), savedProduct.getId());
        assertEquals(product.getName(), savedProduct.getName());
        assertEquals(product.getPrice(), savedProduct.getPrice());
        assertEquals(product.getQuantity(), savedProduct.getQuantity());

        verify(productRepo, times(1)).save(product);
    }
}
