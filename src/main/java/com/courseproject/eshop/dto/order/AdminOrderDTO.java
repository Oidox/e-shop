package com.courseproject.eshop.dto.order;

import com.courseproject.eshop.dto.user.UserDTO;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Аида Есанян
 **/
@Getter
@Setter
public class AdminOrderDTO {
    private Long id;
    private UserDTO userDTO;
}
