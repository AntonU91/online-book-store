package org.example.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.example.dto.BookDto;
import org.example.dto.CreateBookRequestDto;
import org.example.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired
    private BookService bookService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public BookDto save(Authentication authentication,
            @RequestBody @Valid CreateBookRequestDto bookRequestDto) {
        return bookService.save(bookRequestDto);
    }

    @GetMapping
    public List<BookDto> findAll(Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public BookDto getBookById(@PathVariable("id") Long id) {
        return bookService.getBookById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBookById(Authentication authentication, @PathVariable("id") Long id) {
        bookService.deleteBookById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateBookById(Authentication authentication, @PathVariable("id") Long id,
            @RequestBody @Valid CreateBookRequestDto createBookRequestDto) {
        bookService.updateBookById(id, createBookRequestDto);
    }
}
