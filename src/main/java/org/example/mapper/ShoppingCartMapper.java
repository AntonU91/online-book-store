package org.example.mapper;

import org.example.config.MapperConfig;
import org.example.dto.ShoppingCartDto;
import org.example.model.ShoppingCart;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface ShoppingCartMapper {
    ShoppingCartDto toDto(ShoppingCart shoppingCart);
}
