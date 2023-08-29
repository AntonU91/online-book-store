package org.example.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.dto.CartItemRequestDto;
import org.example.dto.ShoppingCartDto;
import org.example.service.ShoppingCartService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
@AllArgsConstructor
public class ShoppingCartController {
    private ShoppingCartService shoppingCartService;

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @PostMapping
    public void addBook(Authentication authentication,
            @RequestBody @Valid CartItemRequestDto cartItemRequestDto) {
        shoppingCartService.addItemToCart(authentication, cartItemRequestDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @GetMapping()
    public ShoppingCartDto getAllCartItems(Authentication authentication) {
        return shoppingCartService.getAllCartItems(authentication);
    }

}
