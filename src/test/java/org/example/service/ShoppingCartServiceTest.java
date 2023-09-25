package org.example.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;
import org.example.dto.CartItemQuantityRequestDto;
import org.example.dto.CartItemRequestDto;
import org.example.dto.CartItemResponseDto;
import org.example.dto.ShoppingCartDto;
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
import org.example.service.impl.ShoppingCartServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceTest {
    private static final String USER_EMAIL = "test@net";
    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private CartItemMapper cartItemMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private ShoppingCartMapper shoppingCartMapper;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    @Mock
    private Authentication authentication;


    @Test
    void addItemToCar_ValidCartItem_ShouldAddCartItem() {
        CartItemRequestDto cartItemRequestDto = new CartItemRequestDto();
        cartItemRequestDto.setBookId(1L);
        cartItemRequestDto.setQuantity(2);

        User user = new User();
        user.setId(1L);

        Book book = new Book();
        book.setId(1L);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);

        CartItem cartItem = new CartItem();
        cartItem.setBook(book);
        cartItem.setQuantity(2);

        when(authentication.getName()).thenReturn(USER_EMAIL);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(user));
        when(shoppingCartRepository.findShoppingCartByUserId(1L)).thenReturn(
                Optional.of(shoppingCart));
        when(cartItemMapper.toEntity(cartItemRequestDto)).thenReturn(cartItem);

        shoppingCartService.addItemToCart(authentication, cartItemRequestDto);
        Mockito.verify(shoppingCartRepository, times(1)).findShoppingCartByUserId(user.getId());
        Mockito.verify(cartItemRepository).save(cartItem);
    }

    @Test
    void getAllCartItems_ShouldReturnShoppingCart() {
        User user = new User();
        user.setId(1L);

        CartItem cartItem1 = new CartItem();
        cartItem1.setId(1L);
        cartItem1.setBook(new Book());
        cartItem1.setQuantity(2);

        CartItem cartItem2 = new CartItem();
        cartItem2.setId(2L);
        cartItem2.setBook(new Book());
        cartItem2.setQuantity(3);
        Set<CartItem> cartItems = Set.of(cartItem1, cartItem2);

        CartItemResponseDto cartItemResponseDto1 = new CartItemResponseDto();
        cartItemResponseDto1.setId(cartItem1.getId());
        cartItemResponseDto1.setQuantity(cartItem1.getQuantity());
        CartItemResponseDto cartItemResponseDto2 = new CartItemResponseDto();
        cartItemResponseDto2.setId(cartItem2.getId());
        cartItemResponseDto1.setQuantity(cartItem2.getQuantity());
        Set<CartItemResponseDto> cartItemResponseDtoSet =
                Set.of(cartItemResponseDto1, cartItemResponseDto2);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);
        shoppingCart.setCartItems(cartItems);

        ShoppingCartDto shoppingCartDto = new ShoppingCartDto();
        shoppingCartDto.setCartItems(cartItemResponseDtoSet);
        shoppingCartDto.setUserId(user.getId());
        shoppingCartDto.setId(1L);

        when(authentication.getName()).thenReturn(USER_EMAIL);
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(user));
        when(shoppingCartRepository.findShoppingCartByUserId(1L)).thenReturn(
                Optional.of(shoppingCart));
        when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(shoppingCartDto);
        when(shoppingCartService.getAllCartItems(authentication)).thenReturn(shoppingCartDto);
        ShoppingCartDto result = shoppingCartService.getAllCartItems(authentication);

        Assertions.assertEquals(2, result.getCartItems().size());
        Assertions.assertEquals(shoppingCartDto.getCartItems(), result.getCartItems());
    }

    @Test
    void updateBookQuantity_ValidQuantity_ShouldUpdate() {
        CartItemQuantityRequestDto cartItemQuantityRequestDto = new CartItemQuantityRequestDto();
        cartItemQuantityRequestDto.setQuantity(20);

        CartItemRequestDto cartItemRequestDto = new CartItemRequestDto();
        cartItemRequestDto.setBookId(1L);
        cartItemRequestDto.setQuantity(5);

        User user = new User();
        user.setId(1L);

        Book book = new Book();
        book.setId(1L);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);
        shoppingCart.setUser(user);

        CartItem existingCartItem = new CartItem();
        existingCartItem.setId(1L);
        existingCartItem.setBook(book);
        existingCartItem.setQuantity(2);
        existingCartItem.setShoppingCart(shoppingCart);

        when(authentication.getName()).thenReturn(USER_EMAIL);
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(user));
        when(shoppingCartRepository.findShoppingCartByUserId(user.getId())).thenReturn(
                Optional.of(shoppingCart));
        when(cartItemRepository.findByIdAndShoppingCartId(existingCartItem.getId(),
                shoppingCart.getId())).thenReturn(Optional.of(existingCartItem));

        shoppingCartService.updateBookQuantity(authentication, existingCartItem.getId(),
                cartItemQuantityRequestDto);
        Mockito.verify(shoppingCartRepository, times(1)).findShoppingCartByUserId(user.getId());
        Mockito.verify(cartItemRepository, times(1))
                .findByIdAndShoppingCartId(existingCartItem.getId(), shoppingCart.getId());
        Mockito.verify(cartItemRepository, times(1)).save(existingCartItem);
    }
}