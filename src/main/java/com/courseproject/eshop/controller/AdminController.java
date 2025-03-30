package com.courseproject.eshop.controller;

import com.courseproject.eshop.dto.product.ProductDTO;
import com.courseproject.eshop.entity.ProductEntity;
import com.courseproject.eshop.exceptions.ProductNotFoundException;
import com.courseproject.eshop.mappers.ProductMapper;
import com.courseproject.eshop.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @author Аида Есанян
 **/
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final ProductMapper productMapper;
    private final ProductService productService;

    /**
     * GET-метод создает форму для добавления нового продукта
     */
    @GetMapping("/add-product")
    public String addProductForm(Model model) {
        model.addAttribute("product", new ProductEntity());
        return "add-product";
    }

    /**
     * POST-метод сохраняет новый продукт и перенаправляет на домашнюю страницу
     */
    @PostMapping("/add-product")
    public String addProduct(@ModelAttribute ProductDTO productDTO) {
        productService.saveProduct(productMapper.toProduct(productDTO));
        return "redirect:/home";
    }

    /**
     * POST-метод удаляет продукт по имени и перенаправляет на домашнюю страницу
     */
    @PostMapping("/delete-product/{name}")
    public String deleteProduct(@PathVariable String name) {
        productService.deleteProductByName(name);
        return "redirect:/home";
    }

    /**
     * GET-метод создает форму для редактирования продукта по имени
     */
    @GetMapping("/update-product/{name}")
    public String updateProductForm(@PathVariable String name, Model model) {
        try {
            model.addAttribute("product", productMapper.toDTO(productService.getProductByName(name)));
        } catch (ProductNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return "update-product";
    }

    /**
     * POST-метод сохраняет изменения продукта и перенаправляет на домашнюю страницу
     */
    @PostMapping("/update-product")
    public String updateProduct(@ModelAttribute("product") ProductDTO productDTO, @RequestParam String currentName) {
        try {
            ProductEntity product = productService.getProductByName(currentName);
            productService.updateProduct(product, productDTO.getName(), productDTO.getPrice(), productDTO.getQuantity());
        } catch (ProductNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return "redirect:/home";
    }
}
