package org.example.service;

import org.example.dto.BookDto;
import org.example.dto.BookDtoWithoutCategoryIds;
import org.example.dto.CreateBookRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto bookRequestDto);

    List<BookDto> findAll(Pageable pageable);

    BookDto getBookById(Long id);

    void deleteBookById(Long id);

    void updateBookById(Long id, CreateBookRequestDto createBookRequestDto);

    List<BookDtoWithoutCategoryIds> findAllByCategoryId(Long categoryId);
}
