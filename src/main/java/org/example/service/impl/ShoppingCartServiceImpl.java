package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.CartItemRequestDto;
import org.example.dto.ShoppingCartDto;
import org.example.exception.EntityNotFoundException;
import org.example.mapper.CartItemMapper;
import org.example.mapper.ShoppingCartMapper;
import org.example.model.Book;
import org.example.model.CartItem;
import org.example.model.ShoppingCart;
import org.example.model.User;
import org.example.repository.BookRepository;
import org.example.repository.CartItemRepository;
import org.example.repository.ShoppingCartRepository;
import org.example.repository.UserRepository;
import org.example.service.ShoppingCartService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final UserRepository userRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final BookRepository bookRepository;

    @Override
    public void addItemToCart(Authentication authentication,
            CartItemRequestDto cartItemRequestDto) {
        Book bookToAddInCart = bookRepository.findById(cartItemRequestDto.getBookId())
                                       .orElseThrow(() -> new EntityNotFoundException(
                                               "Can not find book with id: "
                                               + cartItemRequestDto.getBookId()));
        CartItem entity = cartItemMapper.toEntity(cartItemRequestDto);
        entity.setBook(bookToAddInCart);

        String email = authentication.getName();
        User user = getUser(email);
        ShoppingCart shoppingCart = shoppingCartRepository.findShoppingCartByUserId(user.getId())
                                            .orElseGet(() -> registerNewCart(user));
        entity.setShoppingCart(shoppingCart);
        cartItemRepository.save(entity);
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("Can not find user by email" + email));
    }

    @Override
    public ShoppingCartDto getAllCartItems(Authentication authentication) {
        String email = authentication.getName();
        User user = getUser(email);
        ShoppingCart shoppingCart = shoppingCartRepository.findShoppingCartByUserId(user.getId())
                                            .orElseGet(() -> registerNewCart(user));
        return shoppingCartMapper.toDto(shoppingCart);

    }

    private ShoppingCart registerNewCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
        return shoppingCart;
    }
}
