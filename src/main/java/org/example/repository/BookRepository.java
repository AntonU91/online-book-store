package org.example.repository;

import jakarta.transaction.Transactional;
import java.util.List;
import org.example.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Modifying
    @Query("UPDATE Book b SET b.title = :#{#book.title}, b.price = :#{#book.price}, "
            + "b.isbn = :#{#book.isbn}, "
            + "b.author = :#{#book.author}, b.description = :#{#book.description}, "
            + "b.coverImage = :#{#book.coverImage} WHERE b.id = :id")
    @Transactional
    void updateBookById(@Param("id") Long id, @Param("book") Book book);

    @Query(value = "SELECT * FROM books b JOIN book_category b_k "
                   + "ON b.id=b_k.book_id WHERE b_k.category_id =:categoryId", nativeQuery = true)
    List<Book> findAllByCategoryId(@Param("categoryId") Long categoryId);
}
