package com.courseproject.eshop.controller;

import com.courseproject.eshop.dto.product.ProductDTO;
import com.courseproject.eshop.exceptions.ProductNotFoundException;
import com.courseproject.eshop.mappers.ProductMapper;
import com.courseproject.eshop.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Аида Есанян
 **/
@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    private final ProductMapper productMapper;

    /**
     * GET-метод создает форму продукта с его информацией
     */
    @GetMapping("/{name}")
    public String productCard(@PathVariable String name, Model model) {
        try {
            ProductDTO product = productMapper.toDTO(productService.getProductByName(name));
            model.addAttribute("product", product);
        } catch (ProductNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return "product_card";
    }
}
