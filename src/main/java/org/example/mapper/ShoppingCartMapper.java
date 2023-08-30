package org.example.mapper;

import org.example.config.MapperConfig;
import org.example.dto.ShoppingCartDto;
import org.example.model.ShoppingCart;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public abstract class ShoppingCartMapper {
    public abstract ShoppingCartDto toDto(ShoppingCart shoppingCart);

    @AfterMapping()
    public void setUserId(ShoppingCart shoppingCart,
            @MappingTarget ShoppingCartDto shoppingCartDto) {
        Long userId = shoppingCart.getUser().getId();
        shoppingCartDto.setUserId(userId);
    }

}
