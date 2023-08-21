package org.example.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "books")
@Data
@Where(clause = "is_deleted=FALSE")
@SQLDelete(sql = "UPDATE books SET is_deleted=TRUE WHERE id=?")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotEmpty
    private String title;

    @Column(nullable = false)
    @NotEmpty
    private String author;

    @Column(unique = true, nullable = false)
    @NotEmpty
    private String isbn;

    @Column(nullable = false)
    @PositiveOrZero
    private BigDecimal price;

    private String description;
    private String coverImage;
    private boolean isDeleted;
}
