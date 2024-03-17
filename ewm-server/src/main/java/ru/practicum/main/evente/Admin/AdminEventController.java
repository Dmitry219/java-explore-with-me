package ru.practicum.main.evente.Admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.QueryEvent.AdminEventFilterRequest;
import ru.practicum.main.comments.CommentDtoShort;
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
@RequestMapping(path = "/admin")
@Slf4j
public class AdminEventController {
    private  final EventService eventService;

    public AdminEventController(EventService eventService) {
        this.eventService = eventService;
    }

    //Поиск события
    //Эндпоинт возвращает полную информацию обо всех событиях подходящих под переданные условия
    //В случае, если по заданным фильтрам не найдено ни одного события, возвращает пустой список
    @GetMapping("/events")
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
    @PatchMapping("/events/{eventId}")
    public EventDtoResponse updatingEventDataAndStatus(@Valid @RequestBody EventDtoRequestUpdateStateAction eventDtoRequestUpdateStateAction,
                                                       @PathVariable long eventId) {
        log.info("Admin Проверка контроллер метод updatingAnEventAddedByTheCurrentUser eventId {}", eventId);
        return eventService.updatingEventDataAndStatus(eventDtoRequestUpdateStateAction,eventId);
    }

    //-----Комментарий-----
    @DeleteMapping("/comment/{commitId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateComment(@PathVariable long commitId) {
        log.info("Public Контроллер метод updateComment проверка eventId = {}", commitId);
        eventService.deleteCommentById(commitId);
    }

    @GetMapping("/comment/{commitId}")
    public CommentDtoShort getBuIdComment(@PathVariable long commitId) {
        log.info("Public Контроллер метод getBuIdComment проверка eventId = {}", commitId);
        return eventService.getByIdComment(commitId);
    }

    //всех коменнтарии одного пользовталея
    @GetMapping("/comment")
    public List<CommentDtoShort> getAllByComment(@RequestHeader(value = "User-Id") long userId) {
        log.info("Public Контроллер метод getBuIdComment проверка userId = {}", userId);
        return eventService.getAllByComment(userId);
    }

    //вывести комментарии с бранными словами
    @GetMapping("/comment/searche")
    public List<CommentDtoShort> getAllBySearcheComment(@RequestParam(required = false) String text) {
        log.info("Public Контроллер метод getBuIdComment проверка text = {}", text);
        return eventService.getAllBySearcheComment(text);
    }

}
