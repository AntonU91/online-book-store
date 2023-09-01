package org.example.service;

import org.example.dto.CartItemQuantityRequestDto;
import org.example.dto.CartItemRequestDto;
import org.example.dto.ShoppingCartDto;
import org.springframework.security.core.Authentication;

public interface ShoppingCartService {
    void addItemToCart(Authentication authentication, CartItemRequestDto cartItemRequestDto);

    ShoppingCartDto getAllCartItems(Authentication authentication);

    void updateBookQuantity(Authentication authentication, Long cartItemId,
            CartItemQuantityRequestDto bookQuantityToSave);
}
