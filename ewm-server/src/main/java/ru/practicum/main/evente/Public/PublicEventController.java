package ru.practicum.main.evente.Public;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.QueryEvent.PublicEventFilterRequest;
import ru.practicum.main.comments.CommentDtoShort;
import ru.practicum.main.evente.EventService;
import ru.practicum.main.evente.dto.EventDtoResponse;
import ru.practicum.main.evente.dto.EventShortDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/events")
@Slf4j
public class PublicEventController {
    private final EventService eventService;

    //Получение событий с возможностью фильтраций
    @GetMapping
    public List<EventShortDto> getEvents(@RequestParam(required = false) String text,
                                                                               @RequestParam (required = false)List<Long> categories,
                                                                               @RequestParam (required = false)Boolean paid,
                                                                               @RequestParam(value = "rangeStart", required = false)
                                                                                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                                                LocalDateTime rangeStart,
                                                                               @RequestParam(value = "rangeEnd", required = false)
                                                                                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                                                LocalDateTime rangeEnd,
                                                                               @RequestParam (defaultValue = "false") Boolean onlyAvailable,
                                                                               @RequestParam (defaultValue = "EVENT_DATE") SortType sort,
                                                                               @RequestParam(value = "from", defaultValue = "0") @Positive int from,
                                                                               @RequestParam(value = "size", defaultValue = "10") @Positive int size,
                                                                               HttpServletRequest request) {
        log.info("Public Контроллер метод getReceivingEventsWithThePossibilityOfFiltering проверка text = {}", text);
        log.info("Public Контроллер метод getReceivingEventsWithThePossibilityOfFiltering проверка categories = {}", categories);
        log.info("Public Контроллер метод getReceivingEventsWithThePossibilityOfFiltering проверка paid = {}", paid);
        log.info("Public Контроллер метод getReceivingEventsWithThePossibilityOfFiltering проверка rangeStart = {}", rangeStart);
        log.info("Public Контроллер метод getReceivingEventsWithThePossibilityOfFiltering проверка rangeEnd = {}", rangeEnd);
        log.info("Public Контроллер метод getReceivingEventsWithThePossibilityOfFiltering проверка onlyAvailable = {}", onlyAvailable);
        log.info("Public Контроллер метод getReceivingEventsWithThePossibilityOfFiltering проверка sort = {}", sort);
        log.info("Public Контроллер метод getReceivingEventsWithThePossibilityOfFiltering проверка from = {}", from);
        log.info("Public Контроллер метод getReceivingEventsWithThePossibilityOfFiltering проверка size = {}", size);

        List<EventShortDto> results = eventService.getEvents(PublicEventFilterRequest.builder()
                .categories(categories)
                .paid(paid)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .onlyAvailable(onlyAvailable)
                .sort(sort)
                .from(from)
                .size(size)
                .build(), request);

        return results;
    }

    //получение подробной информации об опубликованном событии по его id
    @GetMapping(path = "/{id}")
    public EventDtoResponse getObtainingDetailedInformationAboutAPublishedEventByItsId(@PathVariable long id, HttpServletRequest request) {
        log.info("Public Контроллер метод getObtainingDetailedInformationAboutAPublishedEventByItsId проверка id = {}", id);

        EventDtoResponse eventDtoResponse = eventService.getObtainingDetailedInformationAboutAPublishedEventByItsId(id, request);

        return eventDtoResponse;
    }

    @GetMapping("/comment/{commitId}")
    public CommentDtoShort getBuIdComment(@PathVariable long commitId) {
        log.info("Public Контроллер метод getBuIdComment проверка eventId = {}", commitId);
        return eventService.getByIdComment(commitId);
    }

    @GetMapping("/comment")
    public List<CommentDtoShort> getBuIdComment() {
        return eventService.getAllComments();
    }
}
