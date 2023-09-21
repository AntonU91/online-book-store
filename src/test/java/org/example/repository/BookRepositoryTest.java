package org.example.repository;

import java.util.List;
import org.example.model.Book;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Find all books by specified category ID")
    @Sql(scripts = {"classpath:db/book/insert-books-to-books-table.sql",
            "classpath:db/category/insert-category-to-categories-table.sql",
            "classpath:db/book_category/insert-data-to-book_category-table.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:db/book_category/delete-data-from-book_category-table.sql",
            "classpath:db/book/delete-books-from-books-table.sql",
            "classpath:db/category/delete-category-from-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllByCategoryId_ValidId_ShouldReturnListOfBook() {
        Long categoryID = 1L;
        List<Book> booksList = bookRepository.getAllByCategoriesId(categoryID);
        Long expectedListSize = 3L;
        Assertions.assertEquals(expectedListSize, booksList.size());
    }
}
