package com.courseproject.eshop.exceptions;

/**
 * @author Аида Есанян
 **/
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
