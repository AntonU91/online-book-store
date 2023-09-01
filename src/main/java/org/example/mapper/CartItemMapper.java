package org.example.mapper;

import org.example.config.MapperConfig;
import org.example.dto.CartItemRequestDto;
import org.example.dto.CartItemResponseDto;
import org.example.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(config = MapperConfig.class)
@Component
public interface CartItemMapper {
    @Mapping(target = "book.id", source = "bookId")
    public abstract CartItem toEntity(CartItemRequestDto cartItemRequestDto);

    @Mapping(target = "bookId", source = "book.id")
    public abstract CartItemRequestDto toRequestDto(CartItem cartItem);

    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "bookTitle", source = "book.title")
    public abstract CartItemResponseDto toResponseDto(CartItem cartItem);
}
