package ru.practicum.main.evente.Public;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.RequestStatsDto;
import ru.practicum.ResponseStatsDto;
import ru.practicum.StatsClient;
import ru.practicum.main.QueryEvent.PublicEventFilterRequest;
import ru.practicum.main.evente.EventService;
import ru.practicum.main.evente.dto.EventDtoResponse;
import ru.practicum.main.evente.dto.EventShortDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PublicEventController {
    private final EventService eventService;
    private final StatsClient statsClient;
    private final ObjectMapper objectMapper;

    //Получение событий с возможностью фильтраций
    //TODO Сделать логику
    @GetMapping(path = "/events")
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
        List<ResponseStatsDto> viwes = new ArrayList<>();
        try {
            RequestStatsDto requestStatsDto = RequestStatsDto.builder()
                    .uri(request.getRequestURI())
                    .ip(request.getRemoteAddr())
                    .app("ewm-main-service")
                    .timestamp(LocalDateTime.now())
                    .build();
            ResponseEntity<Object> stats = statsClient.creatRequestInformation(requestStatsDto);
            log.info("информация статиcтики {}", stats);
            ResponseEntity<Object> infStats = statsClient.getRequestInformation(LocalDateTime.of(2000,1,1,1,1,1),
                    LocalDateTime.now(), List.of(request.getRequestURI()),true);
            TypeReference<List<ResponseStatsDto>> referese = new TypeReference<>(){};
            viwes = objectMapper.convertValue(infStats.getBody(),referese);
            log.info("viwe = {}", viwes.get(0));
        } catch (Exception e) {
            throw new RuntimeException("Что то пошло не так в /events");
        }

        List<EventShortDto> results = eventService.getEvents(PublicEventFilterRequest.builder()
                .categories(categories)
                .paid(paid)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .onlyAvailable(onlyAvailable)
                .sort(sort)
                .from(from)
                .size(size)
                .build());

        return results;
    }

    //получение подробной информации об опубликованном событии по его id
    @GetMapping(path = "/events/{id}")
    public EventDtoResponse getObtainingDetailedInformationAboutAPublishedEventByItsId(@PathVariable long id, HttpServletRequest request) {
        log.info("Public Контроллер метод getObtainingDetailedInformationAboutAPublishedEventByItsId проверка id = {}", id);
        List<ResponseStatsDto> viwes = new ArrayList<>();
        try {
            RequestStatsDto requestStatsDto = RequestStatsDto.builder()
                    .uri(request.getRequestURI())
                    .ip(request.getRemoteAddr())
                    .app("ewm-main-service")
                    .timestamp(LocalDateTime.now())
                    .build();
            ResponseEntity<Object> stats = statsClient.creatRequestInformation(requestStatsDto);
            log.info("информация статиcтики {}", stats);
            ResponseEntity<Object> infStats = statsClient.getRequestInformation(LocalDateTime.of(2000,1,1,1,1,1),
                    LocalDateTime.now(), List.of(request.getRequestURI()),true);
            TypeReference<List<ResponseStatsDto>> referese = new TypeReference<>(){};
            viwes = objectMapper.convertValue(infStats.getBody(),referese);
            log.info("viwe = {}", viwes.get(0));
        } catch (Exception e) {
            throw new RuntimeException("Что то пошло не так в /events/{id}");
        }
        EventDtoResponse eventDtoResponse = eventService.getObtainingDetailedInformationAboutAPublishedEventByItsId(id,viwes);

        return eventDtoResponse;
    }
}
