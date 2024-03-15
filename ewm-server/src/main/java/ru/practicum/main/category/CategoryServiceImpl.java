package ru.practicum.main.category;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.category.dto.CategoryDto;
import ru.practicum.main.evente.EventRepository;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.exception.User.NotFoundExceptionConflictUser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository, EventRepository eventRepository) {
        this.categoryRepository = categoryRepository;
        this.eventRepository = eventRepository;
    }

    @Transactional
    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) throws DataIntegrityViolationException {
            log.info("Проверка сервис метод createCategory categoryDto = {}", categoryDto);
            checkNameCategory(categoryDto.getName());

            Category category = categoryRepository.save(CategoryMapper.toCategory(categoryDto));
            log.info("Проверка сервис метод createCategory category = {}", category);
            return CategoryMapper.toCategoryDto(category);
    }

    @Transactional
    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, long catId) throws DataIntegrityViolationException {
        log.info("Проверка сервис метод updateCategory categoryDto = {}", categoryDto);
        log.info("Проверка сервис метод updateCategory catId = {}", catId);
        checkIdCat(catId);

        Category category = CategoryMapper.toCategory(categoryDto);
        category.setId(catId);
        if (category.getName() == null) {
            category.setName(categoryRepository.findById(catId).get().getName());
        } else {
            if (categoryRepository.findById(catId).get().getName().equals(categoryDto.getName())) {
                return CategoryMapper.toCategoryDto(category);
            }
        }

        log.info("Проверка сервис метод updateCategory category = {}", category);
        Category updateCategory = categoryRepository.save(category);
        return CategoryMapper.toCategoryDto(updateCategory);
    }

    @Override
    public List<CategoryDto> getCategorys(int from, int size) {
        List<Category> categorys = new ArrayList<>();
        PageRequest pageRequest = PageRequest.of(from,size);

        categorys = categoryRepository.findAllBy(pageRequest);
        log.info("Проверка сервис метод getCategorys categorys = {}", categorys);
        if (categorys.isEmpty()) {
            return new ArrayList<>();
        } else {
            return categorys.stream().map(CategoryMapper::toCategoryDto).collect(Collectors.toList());
        }
    }

    @Override
    public CategoryDto getByIdCategory(long catId) {
        checkIdCat(catId);
        return CategoryMapper.toCategoryDto(categoryRepository.findById(catId).get());
    }

    @Transactional
    @Override
    public void deleteCategory(long catId) {
        log.info("Проверка сервис метод deleteCategory catId = {}", catId);
        checkIdCat(catId);
        checkCategoryEquippedInEvent(catId);
        categoryRepository.deleteById(catId);
    }

    //проверка существования категории
    private void checkIdCat(long catId) {
        log.info("Проверка сервис метод checkIdCat проверка ID CATEGORY");
        if (!categoryRepository.existsById(catId)) {
            log.info("Ошибка такого айди нет");
            throw new NotFoundException(String.format("Category с таким id = %s  не существует!", catId));
        }
    }

    //Проверка нахождения категории событии
    private void checkCategoryEquippedInEvent(long catId) {
        log.info("Проверка сервис метод checkCategoryEquippedInEvent проверка ID CATEGORY");
        log.info("Проверка сервис метод checkCategoryEquippedInEvent работы запросы к бд событий = {}",
                eventRepository.findAllByCategoryId(catId));
        if (!eventRepository.findAllByCategoryId(catId).isEmpty()) {
            throw new NotFoundExceptionConflictUser();
        }
    }

    //Проверка уникальности имени
    private void checkNameCategory(String name) {
        log.info("Проверка сервис метод checkNameCategory проверка NAME CATEGORY");
        log.info("Проверка сервис метод checkNameCategory работы запросы к бд категорий name = {}",
                categoryRepository.findByName(name));
        if (categoryRepository.findByName(name) != null) {
            throw new NotFoundExceptionConflictUser();
        }
    }
}
