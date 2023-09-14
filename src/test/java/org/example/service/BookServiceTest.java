package org.example.service;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.example.dto.BookDto;
import org.example.dto.BookDtoWithoutCategoryIds;
import org.example.dto.CreateBookRequestDto;
import org.example.exception.EntityNotFoundException;
import org.example.mapper.BookMapper;
import org.example.model.Book;
import org.example.repository.BookRepository;
import org.example.service.impl.BookServiceImpl;
import org.junit.jupiter.api.AfterEach;
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
class BookServiceTest {
    private Book book1;
    private Book book2;
    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    @BeforeEach
    public void setUp() {
        book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Book 1");
        book1.setAuthor("Author 1");
        book1.setIsbn("12345-566");
        book1.setPrice(new BigDecimal("23.33"));

        book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Book 2");
        book2.setAuthor("Author 1");
        book2.setIsbn("12345-4565");
        book2.setPrice(new BigDecimal("23.33"));
    }

    @AfterEach
    public void cleanUp() {
        book1 = null;
        book2 = null;
    }

    @Test
    @DisplayName("Verify correct user was returned after saving")
    public void saveBook_WithValidFields_ShouldReturnValidBook() {
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

        when(bookMapper.toEntity(bookRequestDto)).thenReturn(book1);
        when(bookRepository.save(book1)).thenReturn(book1);
        when(bookMapper.toDto(book1)).thenReturn(expected);

        BookDto actual = bookService.save(bookRequestDto);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Verify correct book list is returned")
    public void findAll_WithValidPageable_ShouldReturnListOfBooks() {
        List<Book> bookList = new ArrayList<>();
        bookList.add(book1);
        bookList.add(book2);
        Page<Book> bookPage = new PageImpl<>(bookList);
        when(bookRepository.findAll(any(Pageable.class))).thenReturn(bookPage);

        BookDto bookDto1 = new BookDto();
        bookDto1.setId(1L);
        bookDto1.setTitle("Book 1");
        bookDto1.setAuthor("Author 1");
        bookDto1.setIsbn("12345-566");
        bookDto1.setPrice(new BigDecimal("23.33"));
        List<BookDto> expected = new ArrayList<>();
        expected.add(bookDto1);

        BookDto bookDto2 = new BookDto();
        bookDto2.setId(2L);
        bookDto2.setTitle("Book 2");
        bookDto2.setAuthor("Author 1");
        bookDto2.setIsbn("12345-4565");
        bookDto2.setPrice(new BigDecimal("23.33"));
        expected.add(bookDto2);
        when(bookMapper.toDto(book1)).thenReturn(expected.get(0));
        when(bookMapper.toDto(book2)).thenReturn(expected.get(1));

        List<BookDto> actual = bookService.findAll(mock(Pageable.class));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Verify correct book is returned by ID")
    public void getBookById_WithValidId_ShouldReturnBook() {
        Long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book1));

        BookDto expected = new BookDto();
        expected.setId(1L);
        expected.setTitle("Book 1");
        expected.setAuthor("Author 1");
        expected.setIsbn("12345-566");
        expected.setPrice(new BigDecimal("23.33"));

        when(bookMapper.toDto(book1)).thenReturn(expected);
        BookDto actual = bookService.getBookById(bookId);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Verify exception when book is not found by ID")
    public void getBookById_WithInvalidId_ShouldThrowEntityNotFoundException() {
        Long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> bookService.getBookById(bookId));
    }

    @Test
    @DisplayName("Verify book is deleted by ID")
    public void deleteBookById_WithValidId_ShouldDeleteBook() {
        Long bookId = 1L;
        bookService.deleteBookById(bookId);
        verify(bookRepository, times(1)).deleteById(bookId);
    }

    @Test
    @DisplayName("Verify book is updated by ID")
    public void updateBookById_WithValidId_ShouldUpdateBook() {
        CreateBookRequestDto bookRequestDto = new CreateBookRequestDto();
        bookRequestDto.setTitle("Book 1");
        bookRequestDto.setAuthor("Author 1");
        bookRequestDto.setIsbn("12345-566");
        bookRequestDto.setPrice(new BigDecimal("23.33"));
        Long bookId = 1L;
        when(bookMapper.toEntity(bookRequestDto)).thenReturn(book1);
        bookService.updateBookById(bookId, bookRequestDto);

        verify(bookRepository, times(1)).save(book1);
    }

    @Test
    @DisplayName("Verify correct book list is returned by category ID")
    public void findAllByCategoryId_WithValidCategoryId_ShouldReturnListOfBooks() {
        List<Book> bookList = new ArrayList<>();
        bookList.add(book1);

        when(bookRepository.getAllByCategoriesId(anyLong())).thenReturn(bookList);

        BookDtoWithoutCategoryIds bookDtoWithoutCategoryIds = new BookDtoWithoutCategoryIds();
        bookDtoWithoutCategoryIds.setId(1L);
        bookDtoWithoutCategoryIds.setTitle("Book 1");
        bookDtoWithoutCategoryIds.setAuthor("Author 1");
        bookDtoWithoutCategoryIds.setIsbn("12345-566");
        bookDtoWithoutCategoryIds.setPrice(new BigDecimal("23.33"));

        List<BookDtoWithoutCategoryIds> expected = new ArrayList<>();
        expected.add(bookDtoWithoutCategoryIds);
        when(bookMapper.toDtoWithoutCategoryIds(any(Book.class))).thenReturn(expected.get(0));

        List<BookDtoWithoutCategoryIds> actual = bookService.findAllByCategoryId(anyLong());
        Assertions.assertEquals(expected, actual);
    }
}
