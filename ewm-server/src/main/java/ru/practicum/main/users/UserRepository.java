package ru.practicum.main.users;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAllByIdIn(List<Long> ids, PageRequest pageRequest);

    List<User> findAllBy(PageRequest pageRequest);

    User findFirstByEmailIsLike(String nameEmail);
}
