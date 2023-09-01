package org.example.controller;

import org.example.dto.CartItemRequestDto;
import org.example.repository.CartItemRepository;
import org.example.service.ShoppingCartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.CartItemQuantityRequestDto;
import org.example.dto.ShoppingCartDto;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;
    private final CartItemRepository cartItemRepository;

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void addBook(Authentication authentication,
            @RequestBody @Valid CartItemRequestDto cartItemRequestDto) {
        shoppingCartService.addItemToCart(authentication, cartItemRequestDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @GetMapping()
    public ShoppingCartDto getAllCartItems(Authentication authentication) {
        return shoppingCartService.getAllCartItems(authentication);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @PutMapping("/cart-items/{cartItemId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateQuantity(Authentication authentication,
            @PathVariable("cartItemId") Long cartItemId,
            @RequestBody @Valid CartItemQuantityRequestDto cartItemQuantityRequestDto) {
        shoppingCartService.updateBookQuantity(authentication, cartItemId,
                cartItemQuantityRequestDto);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/cart-items/{cartItemId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCartItemById(@PathVariable("cartItemId") Long id) {
        cartItemRepository.deleteById(id);
    }
}
