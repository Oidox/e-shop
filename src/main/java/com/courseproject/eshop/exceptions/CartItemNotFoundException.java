package com.courseproject.eshop.exceptions;

/**
 * @author Аида Есанян
 **/
public class CartItemNotFoundException extends RuntimeException {
    public CartItemNotFoundException(String message) {
        super(message);
    }
}
