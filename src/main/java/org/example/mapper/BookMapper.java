package org.example.mapper;

import org.example.config.MapperConfig;
import org.example.dto.BookDto;
import org.example.dto.BookDtoWithoutCategoryIds;
import org.example.dto.CreateBookRequestDto;
import org.example.model.Book;
import org.example.model.Category;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toEntity(CreateBookRequestDto bookRequestDto);

    BookDtoWithoutCategoryIds toDtoWithoutCategoryIds(Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        bookDto.setCategoriesId(book.getCategories().stream()
                                      .map(Category::getId)
                                      .toList());
    }
    @Named("bookFromId")
    default Book bookFromId(Long id) {
        if (id == null) {
            return null;
        }
        Book book = new Book();
        book.setId(id);
        return book;
    }
}
