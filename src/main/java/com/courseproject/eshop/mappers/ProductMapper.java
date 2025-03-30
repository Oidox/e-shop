package com.courseproject.eshop.mappers;

import com.courseproject.eshop.dto.product.ProductDTO;
import com.courseproject.eshop.entity.ProductEntity;
import org.mapstruct.Mapper;

/**
 * @author Аида Есанян
 **/
@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDTO toDTO(ProductEntity product);

    ProductEntity toProduct(ProductDTO productDTO);
}
