package org.example.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.example.dto.BookDto;
import org.example.dto.CreateBookRequestDto;
import org.example.exception.EntityNotFoundException;
import org.example.service.BookService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {
    public static final Long NON_EXISTED_ID = 100L;
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private BookService bookService;
    private CreateBookRequestDto bookRequestDto;
    private CreateBookRequestDto bookRequestDto2;

    @BeforeEach
    void setUp() {
        bookRequestDto = new CreateBookRequestDto();
        bookRequestDto.setTitle("Book 1");
        bookRequestDto.setAuthor("Author 1");
        bookRequestDto.setIsbn("12345-566");
        bookRequestDto.setPrice(new BigDecimal("23.33"));

        bookRequestDto2 = new CreateBookRequestDto();
        bookRequestDto2.setTitle("Book 2");
        bookRequestDto2.setAuthor("Author 2");
        bookRequestDto2.setIsbn("12345-445245");
        bookRequestDto2.setPrice(new BigDecimal("27.33"));
    }

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                          .apply(SecurityMockMvcConfigurers.springSecurity())
                          .build();
    }

    @Test
    @DisplayName("Create a new book")
    @Sql(scripts = {"classpath:db/book-controller-test/after/delete-books-from-books-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "test", password = "test", roles = {"ADMIN", "USER"})
    void createBook_ValidCreateBookRequestDto_Success() throws Exception {
        BookDto expected = new BookDto();
        expected.setId(1L);
        expected.setTitle("Book 1");
        expected.setAuthor("Author 1");
        expected.setIsbn("12345-566");
        expected.setPrice(new BigDecimal("23.33"));

        String jsonRequest = objectMapper.writeValueAsString(bookRequestDto);

        MvcResult mvcResult = mockMvc.perform(post("/api/books")
                                                      .contentType(MediaType.APPLICATION_JSON)
                                                      .content(jsonRequest))
                                      .andReturn();
        BookDto actual =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BookDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @DisplayName("Get all books")
    @Sql(scripts = {"classpath:db/book-controller-test/after/delete-books-from-books-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "test", password = "test", roles = {"ADMIN", "USER"})
    void getAllBooks_ReturnAllAvailableBook() throws Exception {
        BookDto savedBook1 = bookService.save(bookRequestDto);
        BookDto savedBook2 = bookService.save(bookRequestDto2);

        List<BookDto> expected = new ArrayList<>();
        expected.add(savedBook1);
        expected.add(savedBook2);

        Pageable pageable = PageRequest.of(0, 10);
        MvcResult mvcResult =
                mockMvc.perform(get("/api/books").contentType(MediaType.APPLICATION_JSON)
                                        .param("page", String.valueOf(
                                                pageable.getPageNumber()))
                                        .param("size", String.valueOf(pageable.getPageSize()))
                ).andReturn();
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        System.out.println(jsonResponse);
        List<BookDto> actual =
                objectMapper.readValue(jsonResponse, new TypeReference<List<BookDto>>() {
                });
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Update an existing book")
    @Sql(scripts = {"classpath:db/book-controller-test/after/delete-books-from-books-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "test", password = "test", roles = {"ADMIN", "USER"})
    void updateBook_ValidUpdateBookRequestDto_Success() throws Exception {
        BookDto expected = bookService.save(bookRequestDto);
        expected.setTitle("Updated Title");
        expected.setAuthor("Updated Author");
        expected.setPrice(new BigDecimal("29.99"));

        String jsonRequest = objectMapper.writeValueAsString(bookRequestDto);

        mockMvc.perform(put("/api/books/{id}", expected.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Delete an existing book")
    @Sql(scripts = {"classpath:db/book-controller-test/after/delete-books-from-books-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "test", password = "test", roles = {"ADMIN", "USER"})
    void deleteBook_ValidId_Success() throws Exception {
        BookDto savedBook = bookService.save(bookRequestDto);

        mockMvc.perform(delete("/api/books/{id}", savedBook.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Create a new book with invalid data")
    @Sql(scripts = {"classpath:db/book-controller-test/after/delete-books-from-books-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "test", password = "test", roles = {"ADMIN", "USER"})
    void createBook_InvalidData_BadRequest() throws Exception {
        CreateBookRequestDto invalidBookRequest = new CreateBookRequestDto();
        invalidBookRequest.setTitle("");

        String jsonRequest = objectMapper.writeValueAsString(invalidBookRequest);
        mockMvc.perform(post("/api/books")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Get a book by existed id")
    @Sql(scripts = {"classpath:db/book-controller-test/after/delete-books-from-books-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "test", password = "test", roles = {"ADMIN", "USER"})
    void getBookById_ValidId_Success() throws Exception {
        BookDto expected = bookService.save(bookRequestDto);

        MvcResult mvcResult = mockMvc.perform(get("/api/books/{id}", expected.getId()))
                                      .andExpect(status().isOk())
                                      .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        BookDto actual = objectMapper.readValue(jsonResponse, BookDto.class);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Get a book by non-existed id")
    @Sql(scripts = {"classpath:db/book-controller-test/after/delete-books-from-books-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "test", password = "test", roles = {"ADMIN", "USER"})
    void getBookById_NonExistedId_BadRequest() throws Exception {
        mockMvc.perform(get("/api/books/{id}", NON_EXISTED_ID))
                .andExpect(status().isNotFound());
        Assertions.assertThrows(EntityNotFoundException.class, () -> bookService.getBookById(
                NON_EXISTED_ID));
    }

}
