package org.example.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.config.ContextInitializer;
import java.math.BigDecimal;
import org.example.dto.BookDto;
import org.example.dto.CreateBookRequestDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

//@ContextConfiguration(initializers = ContextInitializer.class)
class BookControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                          .apply(SecurityMockMvcConfigurers.springSecurity())
                          .build();
    }

    @Test
    @DisplayName("Create a new book")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
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
                                                      .content(jsonRequest)
                                                      .contentType(MediaType.APPLICATION_JSON))
                                      .andReturn();
        BookDto actual =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BookDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }
}
