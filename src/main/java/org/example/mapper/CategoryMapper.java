package org.example.mapper;

import org.example.config.MapperConfig;
import org.example.dto.CategoryDto;
import org.example.model.Category;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    Category toEntity(CategoryDto categoryDTO);
}
