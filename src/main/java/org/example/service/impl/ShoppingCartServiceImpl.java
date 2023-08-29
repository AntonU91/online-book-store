package org.example.service.impl;

import java.util.Optional;
import lombok.AllArgsConstructor;
import org.example.dto.CartItemRequestDto;
import org.example.dto.ShoppingCartDto;
import org.example.exception.EntityNotFoundException;
import org.example.mapper.CartItemMapper;
import org.example.mapper.ShoppingCartMapper;
import org.example.model.CartItem;
import org.example.model.ShoppingCart;
import org.example.model.User;
import org.example.repository.CartItemRepository;
import org.example.repository.ShoppingCartRepository;
import org.example.repository.UserRepository;
import org.example.service.ShoppingCartService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private CartItemRepository cartItemRepository;
    private CartItemMapper cartItemMapper;
    private UserRepository userRepository;
    private ShoppingCartRepository shoppingCartRepository;
    private ShoppingCartMapper shoppingCartMapper;

    @Override
    public void addItemToCart(Authentication authentication,
            CartItemRequestDto cartItemRequestDto) {
        CartItem entity = cartItemMapper.toEntity(cartItemRequestDto);
        String email = authentication.getName();
        User user = getUser(email);
        Optional<ShoppingCart> shoppingCartOptional =
                shoppingCartRepository.findShoppingCartByUserId(user.getId());
        ShoppingCart shoppingCart = new ShoppingCart();
        if (shoppingCartOptional.isEmpty()) {
            shoppingCart.setUser(user);
            shoppingCartRepository.save(shoppingCart);
        }
        entity.setShoppingCart(shoppingCart);
        cartItemRepository.save(entity);
    }

    private User getUser(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("Can not find user by email" + email));
        return user;
    }

    @Override
    public ShoppingCartDto getAllCartItems(Authentication authentication) {
        String email = authentication.getName();
        User user = getUser(email);
        ShoppingCart shoppingCart = shoppingCartRepository.findShoppingCartByUserId(user.getId())
                                            .orElseThrow(() -> new EntityNotFoundException(
                                                    "Can not find shopping cart with user id: "
                                                    + user.getId()));
        return shoppingCartMapper.toDto(shoppingCart);

    }
}
