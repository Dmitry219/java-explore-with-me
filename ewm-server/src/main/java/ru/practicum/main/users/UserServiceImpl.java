package ru.practicum.main.users;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.exception.User.NotFoundExceptionConflictUser;
import ru.practicum.main.users.dto.UserDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public UserDto createUser(UserDto userDto) {
      log.info("Сервис createUser проверка UserDto = {}", userDto);
      User userSave = userRepository.save(UserMapper.toUser(userDto));
      return UserMapper.toUserDto(userSave);
    }

    @Override
    public List<UserDto> getUser(List<Long> ids, int from, int size) {
        log.info("Сервис getUser проверка ids = {}", ids);
        List<User> users = new ArrayList<>();
        PageRequest pageRequest = PageRequest.of(from,size);
        if (ids == null) {
            users = userRepository.findAllBy(pageRequest);
            log.info("Сервис getUser проверка запроса без списка id из базы users = {}", users);
        } else {
            users = userRepository.findAllByIdIn(ids, pageRequest);
            log.info("Сервис getUser проверка запроса со списком id из базы users = {}", users);
        }
        return users.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteUser(long userId) {
        log.info("Сервис deleteUser проверка userId = {}", userId);
        checkIdUser(userId);
        userRepository.deleteById(userId);
    }

    private void checkIdUser(long userId) {
        log.info("Проверка сервис метод checkIdUser проверка ID USER");
        if (!userRepository.existsById(userId)) {
            throw new NotFoundExceptionConflictUser(String.format("User с таким id = %s  не существует!", userId));
        }
    }
}
