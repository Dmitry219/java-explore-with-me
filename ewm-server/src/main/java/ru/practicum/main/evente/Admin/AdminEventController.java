package ru.practicum.main.evente.Admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.QueryEvent.AdminEventFilterRequest;
import ru.practicum.main.evente.EventService;
import ru.practicum.main.evente.StatusType;
import ru.practicum.main.evente.dto.EventDtoRequestUpdateStateAction;
import ru.practicum.main.evente.dto.EventDtoResponse;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
public class AdminEventController {
    private  final EventService eventService;

    public AdminEventController(EventService eventService) {
        this.eventService = eventService;
    }

    //Поиск события
    //Эндпоинт возвращает полную информацию обо всех событиях подходящих под переданные условия
    //
    //В случае, если по заданным фильтрам не найдено ни одного события, возвращает пустой список
    @GetMapping(path = "/admin/events")
    public List<EventDtoResponse> searchForAnEvent(@RequestParam(required = false) List<Long> users,
                                                   @RequestParam(required = false) List<StatusType> states,
                                                   @RequestParam(required = false) List<Long> categories,
                                                   @RequestParam(value = "rangeStart", required = false)
                                     @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                     LocalDateTime rangeStart,
                                                   @RequestParam(value = "rangeEnd", required = false)
                                     @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                     LocalDateTime rangeEnd,
                                                   @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero int from,
                                                   @RequestParam(value = "size", defaultValue = "10") @Positive int size) {
        log.info("Admin Контроллер метод SearchForAnEvent проверка users = {}", users);
        log.info("Admin Контроллер метод SearchForAnEvent проверка states = {}", states);
        log.info("Admin Контроллер метод SearchForAnEvent проверка categories = {}", categories);
        log.info("Admin Контроллер метод SearchForAnEvent проверка rangeStart = {}", rangeStart);
        log.info("Admin Контроллер метод SearchForAnEvent проверка rangeEnd = {}", rangeEnd);
        log.info("Admin Контроллер метод SearchForAnEvent проверка from = {}", from);
        log.info("Admin Контроллер метод SearchForAnEvent проверка size = {}", size);

        List<EventDtoResponse> results = eventService.searchForAnEvent(
                AdminEventFilterRequest.builder()
                        .users(users)
                        .states(states)
                        .categories(categories)
                        .rangeStart(rangeStart)
                        .rangeEnd(rangeEnd)
                        .from(from)
                        .size(size)
                        .build());


        return results;
    }

    //Редактирование данных события и его статуса (отклонение/публикация)
    @PatchMapping("/admin/events/{eventId}")
    public EventDtoResponse updatingEventDataAndStatus(@Valid @RequestBody EventDtoRequestUpdateStateAction eventDtoRequestUpdateStateAction,
                                                       @PathVariable long eventId) {
        log.info("Admin Проверка контроллер метод updatingAnEventAddedByTheCurrentUser eventId {}", eventId);
        return eventService.updatingEventDataAndStatus(eventDtoRequestUpdateStateAction,eventId);
    }

}
