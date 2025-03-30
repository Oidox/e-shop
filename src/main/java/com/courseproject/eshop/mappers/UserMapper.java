package com.courseproject.eshop.mappers;

import com.courseproject.eshop.dto.user.UserDTO;
import com.courseproject.eshop.entity.UserEntity;
import org.mapstruct.Mapper;

/**
 * @author Аида Есанян
 **/
@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDTO(UserEntity user);

    UserEntity toUser(UserDTO user);
}
