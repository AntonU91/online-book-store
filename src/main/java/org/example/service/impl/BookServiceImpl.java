package org.example.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.example.dto.BookDto;
import org.example.dto.CreateBookRequestDto;
import org.example.exception.EntityNotFoundException;
import org.example.mapper.BookMapper;
import org.example.model.Book;
import org.example.repository.BookRepository;
import org.example.service.BookService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;

    private BookMapper bookMapper;

    @Override
    public BookDto save(CreateBookRequestDto bookRequestDto) {
        Book book = bookMapper.toModel(bookRequestDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable).stream()
                       .map(book -> bookMapper.toDto(book))
                       .collect(Collectors.toList());
    }

    @Override
    public BookDto getBookById(Long id) {
        Book book = bookRepository.findById(id)
                            .orElseThrow(() -> new EntityNotFoundException(
                                    "Can not find book with id:" + id));
        return bookMapper.toDto(book);
    }

    @Override
    public void deleteBookById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public void updateBookById(Long id, CreateBookRequestDto createBookRequestDto) {
        Book book = bookMapper.toModel(createBookRequestDto);
        bookRepository.updateBookById(id, book);
    }
}
