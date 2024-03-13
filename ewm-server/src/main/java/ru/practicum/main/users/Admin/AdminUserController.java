package ru.practicum.main.users.Admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.users.UserService;
import ru.practicum.main.users.dto.UserDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping(path = "admin/users")
@Slf4j
public class AdminUserController {
    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    //Добавление нового пользователя
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody UserDto userDto) {
        log.info("Контроллер метод createUser проверка userDto = {}", userDto);
        return userService.createUser(userDto);
    }

    //Получение информации о пользователе
    @GetMapping
    public List<UserDto> getUser(@RequestParam(value = "ids", required = false) List<Long> ids,
                                 @RequestParam(value = "from", defaultValue = "0") @Positive int from,
                                 @RequestParam(value = "size", defaultValue = "10") @Positive int size) {
        log.info("Контроллер метод getUser проверка ids = {}", ids);
        log.info("Контроллер метод getUser проверка from = {}", from);
        log.info("Контроллер метод getUser проверка size = {}", size);
        return userService.getUser(ids, from, size);
    }

    //Удаление пользователя
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable long userId) {
        log.info("Контроллер метод deleteUser проверка userId = {}", userId);
        userService.deleteUser(userId);
    }
}
