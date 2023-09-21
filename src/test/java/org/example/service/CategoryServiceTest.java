package org.example.service;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.example.dto.CategoryDto;
import org.example.exception.EntityNotFoundException;
import org.example.mapper.CategoryMapper;
import org.example.model.Category;
import org.example.repository.CategoryRepository;
import org.example.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;
    private Category category1;
    private Category category2;
    private CategoryDto categoryDto1;
    private CategoryDto categoryDto2;

    @BeforeEach
    void setUp() {
        category1 = new Category();
        category1.setId(1L);
        category1.setName("First category");
        category1.setDescription("Description of first category");

        category2 = new Category();
        category2.setId(2L);
        category2.setName("Second category");
        category2.setDescription("Description of second category");

        categoryDto1 = new CategoryDto();
        categoryDto1.setId(1L);
        categoryDto1.setName("First category");
        categoryDto1.setDescription("Description of first category");

        categoryDto2 = new CategoryDto();
        categoryDto2.setId(2L);
        categoryDto2.setName("Second category");
        categoryDto2.setDescription("Description of second category");
    }

    @Test
    @DisplayName("Verify correct category list is returned")
    public void findAll_WithValidPageable_ShouldReturnListOfCategories() {
        List<Category> categories = new ArrayList<>();
        categories.add(category1);
        categories.add(category2);
        Page<Category> categoriesPage = new PageImpl<>(categories);
        when(categoryRepository.findAll(any(Pageable.class))).thenReturn(categoriesPage);

        List<CategoryDto> expected = new ArrayList<>();
        expected.add(categoryDto1);
        expected.add(categoryDto2);

        when(categoryMapper.toDto(category1))
                .thenReturn(categoryDto1);
        when(categoryMapper.toDto(category2))
                .thenReturn(categoryDto2);

        List<CategoryDto> actual = categoryService.findAll(mock(Pageable.class));

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Verify exception when category is not found by ID")
    public void getById_WithInvalidId_ShouldThrowEntityNotFoundException() {
        Long categoryId = 1L;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> categoryService.getById(categoryId));
    }

    @Test
    @DisplayName("Verify category is saved correctly")
    public void saveCategory_WithValidCategoryDto_ShouldReturnSavedCategory() {
        when(categoryMapper.toEntity(categoryDto1)).thenReturn(category1);
        when(categoryRepository.save(category1)).thenReturn(category1);
        when(categoryMapper.toDto(category1)).thenReturn(categoryDto1);
        CategoryDto actual = categoryService.save(categoryDto1);
        Assertions.assertEquals(categoryDto1, actual);
    }

    @Test
    @DisplayName("Verify category is updated correctly")
    public void updateCategory_WithValidIdAndCategoryDto_ShouldReturnUpdatedCategory() {
        Long categoryId = 1L;
        CategoryDto expected = new CategoryDto();
        expected.setId(category1.getId());
        expected.setDescription("Updated description");
        expected.setName(category1.getName());

        Category updated = new Category();
        updated.setId(expected.getId());
        updated.setDescription(expected.getDescription());
        updated.setName(expected.getName());

        when(categoryMapper.toEntity(expected)).thenReturn(updated);
        when(categoryRepository.save(updated)).thenReturn(updated);
        when(categoryMapper.toDto(updated)).thenReturn(expected);

        CategoryDto actual = categoryService.update(categoryId, expected);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Verify category is deleted by ID")
    public void deleteCategoryById_WithValidId_ShouldDeleteCategory() {
        Long categoryId = 1L;
        categoryService.deleteById(categoryId);
        verify(categoryRepository, times(1)).deleteById(categoryId);
    }
}