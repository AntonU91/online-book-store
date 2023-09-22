package org.example.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import org.example.dto.BookDto;
import org.example.dto.CartItemRequestDto;
import org.example.dto.CreateBookRequestDto;
import org.example.dto.ShoppingCartDto;
import org.example.repository.CartItemRepository;
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
    private static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private BookService bookService;
    private BookDto book;
    @Autowired
    private CartItemRepository cartItemRepository;

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
//    @Sql(scripts = {
//            "classpath:db/book/insert-books-to-books-table.sql",
//            "classpath:db/role/insert-roles-to-roles-table.sql",
//            "classpath:db/user/insert-user-to-user-table.sql",
//            "classpath:db/role_user/insert-role_user-to-role_user-table.sql",
//            "classpath:db/shopping_cart/insert-shopping_cart-to-shopping_carts-table.sql",
//            "classpath:db/cart_item/insert-cart_item-to-cart_items-table.sql",
//    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:db/cart_item/delete-cart_items-from-cart_items-table.sql",
            "classpath:db/shopping_cart/delete-shopping_carts-from-shopping_carts-table.sql",
            "classpath:db/role_user/delete-roles_users-from-user_role-table.sql",
            "classpath:db/user/delete-users-from-users-table.sql",
            "classpath:db/role/delete-roles-from-roles-table.sql",
            "classpath:db/book_category/delete-data-from-book_category-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void addBookToCart_ValidCartItemRequest_Success() throws Exception {
       CreateBookRequestDto bookRequestDto = new CreateBookRequestDto();
        bookRequestDto.setTitle("Book 1");
        bookRequestDto.setAuthor("Author 1");
        bookRequestDto.setIsbn("12345-566");
        bookRequestDto.setPrice(new BigDecimal("23.33"));

        BookDto savedBook = bookService.save(bookRequestDto);
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
    @DisplayName("Add a book to the shopping cart")
    @WithMockUser(username = "test@net", password = "test", roles = {"USER", "ADMIN"})
//    @Sql(scripts = {
//            "classpath:db/book/insert-books-to-books-table.sql",
//            "classpath:db/role/insert-roles-to-roles-table.sql",
//            "classpath:db/user/insert-user-to-user-table.sql",
//            "classpath:db/role_user/insert-role_user-to-role_user-table.sql",
//            "classpath:db/shopping_cart/insert-shopping_cart-to-shopping_carts-table.sql",
//            "classpath:db/cart_item/insert-cart_item-to-cart_items-table.sql",
//    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:db/cart_item/delete-cart_items-from-cart_items-table.sql",
            "classpath:db/shopping_cart/delete-shopping_carts-from-shopping_carts-table.sql",
            "classpath:db/role_user/delete-roles_users-from-user_role-table.sql",
            "classpath:db/user/delete-users-from-users-table.sql",
            "classpath:db/role/delete-roles-from-roles-table.sql",
            "classpath:db/book_category/delete-data-from-book_category-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void addBookToCart_InvalidCartItemRequest_BadRequest() throws Exception {
        CartItemRequestDto cartItemRequestDto = new CartItemRequestDto();
        cartItemRequestDto.setBookId(book.getId());

        String jsonRequest = objectMapper.writeValueAsString(cartItemRequestDto);
        mockMvc.perform(post("/api/cart")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Get all items in the shopping cart")
    @WithMockUser(username = "test@net", password = "test", roles = {"USER", "ADMIN"})
//    @Sql(scripts = {
//            "classpath:db/book/insert-books-to-books-table.sql",
//            "classpath:db/role/insert-roles-to-roles-table.sql",
//            "classpath:db/user/insert-user-to-user-table.sql",
//            "classpath:db/role_user/insert-role_user-to-role_user-table.sql",
//            "classpath:db/shopping_cart/insert-shopping_cart-to-shopping_carts-table.sql",
//            "classpath:db/cart_item/insert-cart_item-to-cart_items-table.sql",
//    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:db/cart_item/delete-cart_items-from-cart_items-table.sql",
            "classpath:db/shopping_cart/delete-shopping_carts-from-shopping_carts-table.sql",
            "classpath:db/role_user/delete-roles_users-from-user_role-table.sql",
            "classpath:db/user/delete-users-from-users-table.sql",
            "classpath:db/role/delete-roles-from-roles-table.sql",
            "classpath:db/book_category/delete-data-from-book_category-table.sql"
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

//    @Test
//    @DisplayName("Update quantity of an item in the shopping cart")
//    @WithMockUser(username = "test@net", password = "test", roles = {"USER", "ADMIN"})
//    @Sql(scripts = {
//            "classpath:db/book/insert-books-to-books-table.sql",
//            "classpath:db/role/insert-roles-to-roles-table.sql",
//            "classpath:db/user/insert-user-to-user-table.sql",
//            "classpath:db/role_user/insert-role_user-to-role_user-table.sql",
//            "classpath:db/shopping_cart/insert-shopping_cart-to-shopping_carts-table.sql",
//            "classpath:db/cart_item/insert-cart_item-to-cart_items-table.sql",
//    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//    @Sql(scripts = {
//            "classpath:db/cart_item/delete-cart_items-from-cart_items-table.sql",
//            "classpath:db/shopping_cart/delete-shopping_carts-from-shopping_carts-table.sql",
//            "classpath:db/role_user/delete-roles_users-from-user_role-table.sql",
//            "classpath:db/user/delete-users-from-users-table.sql",
//            "classpath:db/role/delete-roles-from-roles-table.sql",
//    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
//    public void updateCartItemQuantity_ValidRequest_Success() throws Exception {
//        CartItemQuantityRequestDto cartItemQuantityRequestDto = new CartItemQuantityRequestDto();
//        cartItemQuantityRequestDto.setQuantity(3);
//     //   CartItem beforeChangeQuantityCartItem = cartItemRepository.findById(1L).get();
//        mockMvc.perform(MockMvcRequestBuilders.put("/api/cart/cart-items/1")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(objectMapper.writeValueAsString(
//                                        cartItemQuantityRequestDto)))
//                .andExpect(status().isOk());
//        CartItem actual = cartItemRepository.findById(1L).get();
//      //  assertNotEquals(beforeChangeQuantityCartItem.getQuantity(), actual.getQuantity());
//        assertEquals(3, actual.getQuantity());
//    }
//
//    @Test
//    @DisplayName("Delete an item from the shopping cart")
//    @WithMockUser(username = "test@net", password = "test", roles = {"USER", "ADMIN"})
//    public void deleteCartItemById_ValidId_Success() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.delete("/api/cart/cart-items/1")
//                                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//    }
}