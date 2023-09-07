package org.example.mapper;

import org.example.config.MapperConfig;
import org.example.dto.OrderItemDto;
import org.example.dto.OrderItemResponseDto;
import org.example.model.CartItem;
import org.example.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mapping(target = "book", source = "book")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "price", source = "book.price")
    OrderItemDto toDto(CartItem cartItem);

    OrderItem toEntity(OrderItemDto orderItemDto);

    @Mapping(target = "bookId", source = "book.id")
    OrderItemResponseDto toResponseDto(OrderItem orderItem);
}
