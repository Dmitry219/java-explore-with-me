package ru.practicum.main.evente;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>,
        JpaSpecificationExecutor<Event>,QuerydslPredicateExecutor<Event> {

    List<Event> findAllByInitiatorId(Long userId, PageRequest pageRequest);

    List<Event> findAllByInitiatorId(Long userId);

    Event findByInitiatorIdAndId(Long userId, Long eventId);

    List<Event> findAllByIdIn(List<Long> eventIds);

    //поиск события по id категории
    List<Event> findAllByCategoryId(Long catId);

}
