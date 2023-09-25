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
    private static final Long EXPECTED_LIST_SIZE = 3L;
    private static final Long CATEGORY_ID = 1L;
    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Find all books by specified category ID")
    @Sql(scripts = {"classpath:db/book-repo-tests/before/insert-books-to-books-table.sql",
            "classpath:db/book-repo-tests/before/insert-categories-to-categories-table.sql",
            "classpath:db/book-repo-tests/before/insert-book_category-to-book_category-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:db/book-repo-tests/after/delete-book_category-from-book_category-table.sql",
            "classpath:db/book-repo-tests/after/delete-books-from-books-table.sql",
            "classpath:db/book-repo-tests/after/delete-category-from-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllByCategoryId_ValidId_ShouldReturnListOfBook() {
        List<Book> booksList = bookRepository.getAllByCategoriesId(CATEGORY_ID);
        Assertions.assertEquals(EXPECTED_LIST_SIZE, booksList.size());
    }
}
