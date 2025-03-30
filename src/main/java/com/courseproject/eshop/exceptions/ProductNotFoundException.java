package com.courseproject.eshop.exceptions;

/**
 * @author Аида Есанян
 **/
public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String message) {
        super(message);
    }
}
