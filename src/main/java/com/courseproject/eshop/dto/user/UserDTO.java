package com.courseproject.eshop.dto.user;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author Аида Есанян
 **/
@Getter
@Setter
public class UserDTO {
    private String firstName;
    private String lastName;
    private String email;
    private BigDecimal balance;
}
