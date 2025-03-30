package com.courseproject.eshop.exceptions;

/**
 * @author Аида Есанян
 **/
public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException(String message) {
        super(message);
    }
}
