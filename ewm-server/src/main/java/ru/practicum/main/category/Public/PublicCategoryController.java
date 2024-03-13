package ru.practicum.main.category.Public;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.category.CategoryService;
import ru.practicum.main.category.dto.CategoryDto;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@Slf4j
public class PublicCategoryController {
    private final CategoryService categoryService;

    public PublicCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    //Получение категории
    @GetMapping("categories")
    public List<CategoryDto> getCategory(@RequestParam(value = "from", defaultValue = "0") @Positive int from,
                                     @RequestParam(value = "size", defaultValue = "10") @Positive int size) {
        log.info("Контроллер метод getCategory проверка from = {}", from);
        log.info("Контроллер метод getCategory проверка size = {}", size);
        return categoryService.getCategorys(from, size);
    }

    //Получение информации о категории по её id
    @GetMapping("categories/{catId}")
    public CategoryDto getByIdCategory(@PathVariable long catId) {
        log.info("Контроллер метод getByIdCategory проверка catId = {}", catId);
        return categoryService.getByIdCategory(catId);
    }

}
