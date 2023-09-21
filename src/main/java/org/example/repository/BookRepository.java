package org.example.repository;

import java.util.List;
import org.example.model.Book;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    @EntityGraph(attributePaths = "categories")
    List<Book> getAllByCategoriesId(Long categoryId);

}
