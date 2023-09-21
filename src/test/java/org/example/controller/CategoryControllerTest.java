package org.example.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.example.dto.CategoryDto;
import org.example.mapper.BookMapper;
import org.example.service.CategoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoryService categoryService;

    private CategoryDto categoryDto1;
    private CategoryDto categoryDto2;
    @Autowired
    private BookMapper bookMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext).build();
    }

    @BeforeEach
    void setUp() {
        categoryDto1 = new CategoryDto();
        categoryDto1.setName("First category");
        categoryDto1.setDescription("Description of first category");

        categoryDto2 = new CategoryDto();
        categoryDto2.setName("Second category");
        categoryDto2.setDescription("Description of second category");
    }

    @Test
    @DisplayName("Create a new category")
    @Sql(scripts = {"classpath:db/category/delete-category-from-categories-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createCategory_ValidCategoryDto_Success() throws Exception {
        CategoryDto expected = categoryService.save(categoryDto1);
        String jsonRequest = objectMapper.writeValueAsString(categoryDto1);

        MvcResult mvcResult = mockMvc.perform(post("/api/categories")
                                                      .contentType(MediaType.APPLICATION_JSON)
                                                      .content(jsonRequest))
                                      .andExpect(status().isOk())
                                      .andReturn();
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        CategoryDto actual = objectMapper.readValue(jsonResponse, CategoryDto.class);
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Test
    @DisplayName("Create a new category with invalid data")
    @Sql(scripts = {"classpath:db/category/delete-category-from-categories-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createCategory_InvalidData_BadRequest() throws Exception {
        CategoryDto invalidCategoryRequest = new CategoryDto();
        invalidCategoryRequest.setDescription("Some description");
        invalidCategoryRequest.setName("inv");

        String jsonRequest = objectMapper.writeValueAsString(invalidCategoryRequest);

        mockMvc.perform(post("/api/categories")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(MvcResult::getResolvedException);
    }

    @Test
    @DisplayName("Get all categories")
    @Sql(scripts = {"classpath:db/category/delete-category-from-categories-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAllCategories_ReturnAllCategories() throws Exception {
        CategoryDto savedCategory1 = categoryService.save(categoryDto1);
        CategoryDto savedCategory2 = categoryService.save(categoryDto2);
        List<CategoryDto> expected = new ArrayList<>();
        expected.add(savedCategory1);
        expected.add(savedCategory2);

        MvcResult mvcResult = mockMvc.perform(get("/api/categories")
                                                      .contentType(MediaType.APPLICATION_JSON))
                                      .andExpect(status().isOk())
                                      .andReturn();
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        List<CategoryDto> actual =
                objectMapper.readValue(jsonResponse, new TypeReference<List<CategoryDto>>() {
                });
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Update a category")
    @Sql(scripts = {"classpath:db/category/delete-category-from-categories-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateCategory_ValidCategoryDto_Success() throws Exception {
        CategoryDto savedCategory = categoryService.save(categoryDto1);
        savedCategory.setName("Updated Category");

        String jsonRequest = objectMapper.writeValueAsString(savedCategory);

        mockMvc.perform(post("/api/categories/{id}", savedCategory.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(equalTo("Updated Category"))));
    }

    @Test
    @DisplayName("Delete a category")
    @Sql(scripts = {"classpath:db/category/delete-category-from-categories-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteCategory_ValidId_Success() throws Exception {
        CategoryDto savedCategory = categoryService.save(categoryDto1);

        mockMvc.perform(delete("/api/categories/{id}", savedCategory.getId())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/categories/{id}", savedCategory.getId())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}

