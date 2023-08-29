package org.example.mapper;

import org.example.config.MapperConfig;
import org.example.dto.CartItemRequestDto;
import org.example.dto.CartItemResponseDto;
import org.example.exception.EntityNotFoundException;
import org.example.model.Book;
import org.example.model.CartItem;
import org.example.repository.BookRepository;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Mapper(config = MapperConfig.class)
@Component
public abstract class CartItemMapper {
    @Autowired
    private BookRepository bookRepository;

    public CartItem toEntity(CartItemRequestDto cartItemRequestDto) {
        Book book = bookRepository.findById(cartItemRequestDto.getBookId())
                            .orElseThrow(() -> new EntityNotFoundException(
                                    "Can not find book with id:" + cartItemRequestDto.getBookId()));
        CartItem cartItem = new CartItem();
        cartItem.setBook(book);
        cartItem.setQuantity(cartItemRequestDto.getQuantity());
        return cartItem;
    }

    public CartItemRequestDto toRequestDto(CartItem cartItem) {
        CartItemRequestDto cartItemRequestDto = new CartItemRequestDto();
        cartItemRequestDto.setQuantity(cartItem.getQuantity());
        cartItemRequestDto.setBookId(cartItem.getBook().getId());
        return cartItemRequestDto;
    }

    public abstract CartItemResponseDto toResponseDto(CartItem cartItem);

    @AfterMapping()
    public void addAdditionalFields(CartItem cartItem,
            @MappingTarget CartItemResponseDto cartItemResponseDto) {
        Book book = cartItem.getBook();
        cartItemResponseDto.setBookId(book.getId());
        cartItemResponseDto.setBookTitle(book.getTitle());
    }
}
