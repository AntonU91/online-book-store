package org.example.repository;

import jakarta.transaction.Transactional;
import org.example.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Modifying
    @Query("UPDATE Category c SET c.name = :#{#category.name}, "
            + "c.description=:#{#category.description} "
            + "WHERE c.id = :id")
    @Transactional
    void updateCategoryById(@Param("id") Long id, @Param("category") Category category);

}
