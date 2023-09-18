package org.example.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.example.dto.BookDto;
import org.example.dto.CreateBookRequestDto;
import org.example.service.BookService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureMockMvc
//@ContextConfiguration(initializers = ContextInitializer.class)
class BookControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private BookService bookService;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                          //.apply(SecurityMockMvcConfigurers.springSecurity())
                          .build();
    }

    @Test
    @DisplayName("Create a new book")
    void createBook_ValidCreateBookRequestDto_Success() throws Exception {
        CreateBookRequestDto bookRequestDto = new CreateBookRequestDto();
        bookRequestDto.setTitle("Book 1");
        bookRequestDto.setAuthor("Author 1");
        bookRequestDto.setIsbn("12345-566");
        bookRequestDto.setPrice(new BigDecimal("23.33"));

        BookDto expected = new BookDto();
        expected.setId(1L);
        expected.setTitle("Book 1");
        expected.setAuthor("Author 1");
        expected.setIsbn("12345-566");
        expected.setPrice(new BigDecimal("23.33"));

        String jsonRequest = objectMapper.writeValueAsString(bookRequestDto);

        MvcResult mvcResult = mockMvc.perform(post("/api/books")
                                                      .contentType(MediaType.APPLICATION_JSON)
                                                      .content(jsonRequest)
                )
                                      .andReturn();
        BookDto actual =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BookDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @DisplayName("Get all books")
    void getAllBooks_ReturnAllAvailableBook() throws Exception {
        CreateBookRequestDto bookRequestDto = new CreateBookRequestDto();
        bookRequestDto.setTitle("Book 1");
        bookRequestDto.setAuthor("Author 1");
        bookRequestDto.setIsbn("12345-566");
        bookRequestDto.setPrice(new BigDecimal("21.33"));

        CreateBookRequestDto bookRequestDto2 = new CreateBookRequestDto();
        bookRequestDto2.setTitle("Book 2");
        bookRequestDto2.setAuthor("Author 2");
        bookRequestDto2.setIsbn("12345-445");
        bookRequestDto2.setPrice(new BigDecimal("27.33"));
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

}
