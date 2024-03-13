package ru.practicum.main.category.Admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.category.CategoryService;
import ru.practicum.main.category.dto.CategoryDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@Slf4j
public class AdminCategoryController {
    private final CategoryService categoryService;

    public AdminCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    //Добавление новой категории
    @PostMapping("/admin/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        log.info("Контроллер метод createCreate проверка categoryDto = {}", categoryDto);
        return categoryService.createCategory(categoryDto);
    }

    //Изминение категории
    @PatchMapping("/admin/categories/{catId}")
    public CategoryDto updateCategory(@Valid @RequestBody CategoryDto categoryDto,
                                      @PathVariable @Positive long catId) {
        log.info("Контроллер метод updateCategory проверка categoryDto = {}", categoryDto);
        log.info("Контроллер метод updateCategory проверка catId = {}", catId);
        return categoryService.updateCategory(categoryDto, catId);
    }

    //Удаление категории
    @DeleteMapping("/admin/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable long catId) {
        log.info("Контроллер метод deleteCategory проверка catId = {}", catId);
        categoryService.deleteCategory(catId);
    }
}
