package ru.practicum.main.users;

import ru.practicum.main.users.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);

    List<UserDto> getUser(List<Long> ids, int from, int size);

    void deleteUser(long userId);
}
