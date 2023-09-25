package org.example.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import org.example.dto.BookDto;
import org.example.dto.CartItemQuantityRequestDto;
import org.example.dto.CartItemRequestDto;
import org.example.dto.CartItemResponseDto;
import org.example.dto.CreateBookRequestDto;
import org.example.dto.ShoppingCartDto;
import org.example.service.BookService;
import org.example.service.ShoppingCartService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
public class ShoppingCartControllerTest {
    private static final int CART_ITEM_ID = 2;
    private static final int EXPECTED_QUANTITY = 3;
    public static final int NON_EXISTED_ID = 1000;
    private static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private BookService bookService;

    @BeforeAll
    private static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                          .apply(SecurityMockMvcConfigurers.springSecurity())
                          .build();
    }

    @BeforeEach
    public void setUp() {
    }

    @Test
    @DisplayName("Add a book to the shopping cart")
    @WithMockUser(username = "test@net", password = "test", roles = {"USER", "ADMIN"})
    @Sql(scripts = {
            "classpath:db/shopping-cart-controller-tests/before/insert-books-to-books-table.sql",
            "classpath:db/shopping-cart-controller-tests/before/insert-user-to-user-table.sql",
            "classpath:db/shopping-cart-controller-tests/before/insert-role_user-to-role_user-table.sql",
            "classpath:db/shopping-cart-controller-tests/before/insert-shopping_cart-to-shopping_carts-table.sql",
            "classpath:db/shopping-cart-controller-tests/before/insert-cart_item-to-cart_items-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:db/shopping-cart-controller-tests/after/delete-cart_items-from-cart_items-table.sql",
            "classpath:db/shopping-cart-controller-tests/after/delete-shopping_carts-from-shopping_carts-table.sql",
            "classpath:db/shopping-cart-controller-tests/after/delete-roles_users-from-user_role-table.sql",
            "classpath:db/shopping-cart-controller-tests/after/delete-users-from-users-table.sql",
            "classpath:db/shopping-cart-controller-tests/after/delete-books-from-books-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void addBookToCart_ValidCartItemRequest_Success() throws Exception {
        CreateBookRequestDto bookRequestDto = new CreateBookRequestDto();
        bookRequestDto.setTitle("Book 1");
        bookRequestDto.setAuthor("Author 1");
        bookRequestDto.setIsbn("12345-566");
        bookRequestDto.setPrice(new BigDecimal("23.33"));

        BookDto savedBook = bookService.getBookById(1L);
        CartItemRequestDto cartItemRequestDto = new CartItemRequestDto();
        cartItemRequestDto.setBookId(savedBook.getId());
        cartItemRequestDto.setQuantity(10);
        String jsonRequest = objectMapper.writeValueAsString(cartItemRequestDto);
        mockMvc.perform(post("/api/cart")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Add an invalid book item to the shopping cart")
    @WithMockUser(username = "test@net", password = "test", roles = {"USER", "ADMIN"})
    @Sql(scripts = {
            "classpath:db/shopping-cart-controller-tests/before/insert-books-to-books-table.sql",
            "classpath:db/shopping-cart-controller-tests/before/insert-user-to-user-table.sql",
            "classpath:db/shopping-cart-controller-tests/before/insert-role_user-to-role_user-table.sql",
            "classpath:db/shopping-cart-controller-tests/before/insert-shopping_cart-to-shopping_carts-table.sql",
            "classpath:db/shopping-cart-controller-tests/before/insert-cart_item-to-cart_items-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:db/shopping-cart-controller-tests/after/delete-cart_items-from-cart_items-table.sql",
            "classpath:db/shopping-cart-controller-tests/after/delete-shopping_carts-from-shopping_carts-table.sql",
            "classpath:db/shopping-cart-controller-tests/after/delete-roles_users-from-user_role-table.sql",
            "classpath:db/shopping-cart-controller-tests/after/delete-users-from-users-table.sql",
            "classpath:db/shopping-cart-controller-tests/after/delete-books-from-books-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void addBookToCart_InvalidCartItemRequest_BadRequest() throws Exception {
        CartItemRequestDto cartItemRequestDto = new CartItemRequestDto();
        cartItemRequestDto.setBookId(1L);
        String jsonRequest = objectMapper.writeValueAsString(cartItemRequestDto);
        mockMvc.perform(post("/api/cart")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Get all items in the shopping cart")
    @WithMockUser(username = "test@net", password = "test", roles = {"USER", "ADMIN"})
    @Sql(scripts = {
            "classpath:db/shopping-cart-controller-tests/before/insert-books-to-books-table.sql",
            "classpath:db/shopping-cart-controller-tests/before/insert-user-to-user-table.sql",
            "classpath:db/shopping-cart-controller-tests/before/insert-role_user-to-role_user-table.sql",
            "classpath:db/shopping-cart-controller-tests/before/insert-shopping_cart-to-shopping_carts-table.sql",
            "classpath:db/shopping-cart-controller-tests/before/insert-cart_item-to-cart_items-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:db/shopping-cart-controller-tests/after/delete-cart_items-from-cart_items-table.sql",
            "classpath:db/shopping-cart-controller-tests/after/delete-shopping_carts-from-shopping_carts-table.sql",
            "classpath:db/shopping-cart-controller-tests/after/delete-roles_users-from-user_role-table.sql",
            "classpath:db/shopping-cart-controller-tests/after/delete-users-from-users-table.sql",
            "classpath:db/shopping-cart-controller-tests/after/delete-books-from-books-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllCartItems_ReturnShoppingCartDto_Success() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ShoppingCartDto expected = shoppingCartService.getAllCartItems(authentication);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/cart")
                                                      .contentType(MediaType.APPLICATION_JSON))
                                      .andExpect(status().isOk())
                                      .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        ShoppingCartDto actual = objectMapper.readValue(jsonResponse, ShoppingCartDto.class);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Update quantity of an item in the shopping cart")
    @WithMockUser(username = "test@net", password = "test", roles = {"USER", "ADMIN"})
    @Sql(scripts = {
            "classpath:db/shopping-cart-controller-tests/before/insert-books-to-books-table.sql",
            "classpath:db/shopping-cart-controller-tests/before/insert-user-to-user-table.sql",
            "classpath:db/shopping-cart-controller-tests/before/insert-role_user-to-role_user-table.sql",
            "classpath:db/shopping-cart-controller-tests/before/insert-shopping_cart-to-shopping_carts-table.sql",
            "classpath:db/shopping-cart-controller-tests/before/insert-cart_item-to-cart_items-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:db/shopping-cart-controller-tests/after/delete-cart_items-from-cart_items-table.sql",
            "classpath:db/shopping-cart-controller-tests/after/delete-shopping_carts-from-shopping_carts-table.sql",
            "classpath:db/shopping-cart-controller-tests/after/delete-roles_users-from-user_role-table.sql",
            "classpath:db/shopping-cart-controller-tests/after/delete-users-from-users-table.sql",
            "classpath:db/shopping-cart-controller-tests/after/delete-books-from-books-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateCartItemQuantity_ValidRequest_Success() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CartItemQuantityRequestDto cartItemQuantityRequestDto = new CartItemQuantityRequestDto();
        cartItemQuantityRequestDto.setQuantity(EXPECTED_QUANTITY);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/cart/cart-items/{id}", CART_ITEM_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(
                                        cartItemQuantityRequestDto)))
                .andExpect(status().isOk());
        ShoppingCartDto allCartItems = shoppingCartService.getAllCartItems(authentication);
        CartItemResponseDto cartItemResponseDto1 = allCartItems.getCartItems().stream()
                                                           .filter(cartItemResponseDto ->
                                                                           cartItemResponseDto.getQuantity() ==
                                                                           EXPECTED_QUANTITY)
                                                           .findFirst().get();
        assertEquals(EXPECTED_QUANTITY, cartItemResponseDto1.getQuantity());
    }

    @Test
    @DisplayName("Delete an item from the shopping cart")
    @WithMockUser(username = "test@net", password = "test", roles = {"USER", "ADMIN"})
    @Sql(scripts = {
            "classpath:db/shopping-cart-controller-tests/before/insert-books-to-books-table.sql",
            "classpath:db/shopping-cart-controller-tests/before/insert-user-to-user-table.sql",
            "classpath:db/shopping-cart-controller-tests/before/insert-role_user-to-role_user-table.sql",
            "classpath:db/shopping-cart-controller-tests/before/insert-shopping_cart-to-shopping_carts-table.sql",
            "classpath:db/shopping-cart-controller-tests/before/insert-cart_item-to-cart_items-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:db/shopping-cart-controller-tests/after/delete-cart_items-from-cart_items-table.sql",
            "classpath:db/shopping-cart-controller-tests/after/delete-shopping_carts-from-shopping_carts-table.sql",
            "classpath:db/shopping-cart-controller-tests/after/delete-roles_users-from-user_role-table.sql",
            "classpath:db/shopping-cart-controller-tests/after/delete-users-from-users-table.sql",
            "classpath:db/shopping-cart-controller-tests/after/delete-books-from-books-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteCartItemById_ValidId_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/cart/cart-items/{id}", CART_ITEM_ID)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Update quantity of an item in the shopping cart")
    @WithMockUser(username = "test@net", password = "test", roles = {"USER", "ADMIN"})
    @Sql(scripts = {
            "classpath:db/shopping-cart-controller-tests/before/insert-books-to-books-table.sql",
            "classpath:db/shopping-cart-controller-tests/before/insert-user-to-user-table.sql",
            "classpath:db/shopping-cart-controller-tests/before/insert-role_user-to-role_user-table.sql",
            "classpath:db/shopping-cart-controller-tests/before/insert-shopping_cart-to-shopping_carts-table.sql",
            "classpath:db/shopping-cart-controller-tests/before/insert-cart_item-to-cart_items-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:db/shopping-cart-controller-tests/after/delete-cart_items-from-cart_items-table.sql",
            "classpath:db/shopping-cart-controller-tests/after/delete-shopping_carts-from-shopping_carts-table.sql",
            "classpath:db/shopping-cart-controller-tests/after/delete-roles_users-from-user_role-table.sql",
            "classpath:db/shopping-cart-controller-tests/after/delete-users-from-users-table.sql",
            "classpath:db/shopping-cart-controller-tests/after/delete-books-from-books-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateCartItemQuantity_InvalidRequest_NotFound() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CartItemQuantityRequestDto cartItemQuantityRequestDto = new CartItemQuantityRequestDto();
        cartItemQuantityRequestDto.setQuantity(EXPECTED_QUANTITY);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/cart/cart-items/{id}", NON_EXISTED_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(
                                        cartItemQuantityRequestDto)))
                .andExpect(status().isNotFound());
    }
}
