package ru.practicum.main.category;

import ru.practicum.main.category.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(CategoryDto categoryDto, long catId);

    void deleteCategory(long catId);

    List<CategoryDto> getCategorys(int from, int size);

    CategoryDto getByIdCategory(long catId);
}
