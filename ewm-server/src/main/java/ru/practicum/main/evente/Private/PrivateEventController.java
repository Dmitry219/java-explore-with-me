package ru.practicum.main.evente.Private;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.evente.EventService;
import ru.practicum.main.evente.dto.*;
import ru.practicum.main.request.dto.RequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@Slf4j
public class PrivateEventController {
    private  final EventService eventService;

    public PrivateEventController(EventService eventService) {
        this.eventService = eventService;
    }

    //Добавление нового события
    @PostMapping(path = "/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventDtoResponse createEvent(@Valid @RequestBody EventDtoRequest eventDtoRequest,
                                        @PathVariable long userId) {
        log.info("Контроллер метод createEvent проверка eventDto = {}", eventDtoRequest);
        log.info("Контроллер метод createEvent проверка userId = {}", userId);
        EventDtoResponse eventDtoResponse = eventService.createEvent(eventDtoRequest, userId);
        log.info("Контроллер метод createEvent проверка сохранения события eventDtoResponse = {}", eventDtoResponse);
        return eventDtoResponse;
    }

    //Получение событий, добавленных текущим пользователем
    @GetMapping(path = "/{userId}/events")
    public List<EventShortDto> getReceivingEventsAddedByTheCurrentUser(@PathVariable long userId,
                                                                       @RequestParam(value = "from", defaultValue = "0") @Positive int from,
                                                                       @RequestParam(value = "size", defaultValue = "10") @Positive int size) {
        log.info("Контроллер метод getReceivingEventsAddedByTheCurrentUser проверка ids = {}", userId);
        log.info("Контроллер метод getReceivingEventsAddedByTheCurrentUser проверка from = {}", from);
        log.info("Контроллер метод getReceivingEventsAddedByTheCurrentUser проверка size = {}", size);
        return eventService.getReceivingEventsAddedByTheCurrentUser(userId, from, size);
    }

    //Получение полной информации о событии добавленном текущим пользователем
    @GetMapping(path = "/{userId}/events/{eventId}")
    public EventDtoResponse getObtainingCompleteInformationAboutTheEventByTheAddedCurrentUser(@PathVariable long userId,
                                                                       @PathVariable long eventId) {
        log.info("Контроллер метод getObtainingCompleteInformationAboutTheEventByTheAddedCurrentUser проверка ids = {}", userId);
        log.info("Контроллер метод getObtainingCompleteInformationAboutTheEventByTheAddedCurrentUser проверка eventId = {}", eventId);

        return eventService.getObtainingCompleteInformationAboutTheEventByTheAddedCurrentUser(userId, eventId);
    }

    //Изминение события добавленного текущим пользователем
    @PatchMapping("/{userId}/events/{eventId}")
    public EventDtoResponse updatingAnEventAddedByTheCurrentUser(@Valid @RequestBody EventDtoRequestUpdateStateAction eventDtoRequestUpdateStateAction,
                                                                 @PathVariable long userId,
                                                                 @PathVariable long eventId) {
        log.info("Проверка контроллер метод updatingAnEventAddedByTheCurrentUser userId {}", userId);
        log.info("Проверка контроллер метод updatingAnEventAddedByTheCurrentUser eventId {}", eventId);

        return eventService.updatingAnEventAddedByTheCurrentUser(eventDtoRequestUpdateStateAction,userId,eventId);
    }

    //Получение информации о запросах на участие в событии текущего пользователя
    @GetMapping(path = "/{userId}/events/{eventId}/requests")
    public List<RequestDto> getObtainingInformationAboutRequestsToParticipateInAnEventOfTheCurrentUser(@PathVariable long userId,
                                                                                                       @PathVariable long eventId) {
        log.info("Контроллер метод getObtainingInformationAboutRequestsToParticipateInAnEventOfTheCurrentUser проверка userId = {}", userId);
        log.info("Контроллер метод getObtainingInformationAboutRequestsToParticipateInAnEventOfTheCurrentUser проверка eventId = {}", eventId);
        return eventService.getObtainingInformationAboutRequestsToParticipateInAnEventOfTheCurrentUser(userId, eventId);
    }

    //Изминение статуса (подтверждена, отменена) заявок на участие в событии текущего пользователя
    @PatchMapping(path = "/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateChangingTheStatusOfApplicationsForParticipationInAnEventForTheCurrentUser(@RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest,
                                                                                                @PathVariable long userId,
                                                                                                @PathVariable long eventId) {
        log.info("Контроллер метод updateChangingTheStatusOfApplicationsForParticipationInAnEventForTheCurrentUser проверка eventRequestStatusUpdateRequest = {}", eventRequestStatusUpdateRequest);
        log.info("Контроллер метод updateChangingTheStatusOfApplicationsForParticipationInAnEventForTheCurrentUser проверка userId = {}", userId);
        log.info("Контроллер метод updateChangingTheStatusOfApplicationsForParticipationInAnEventForTheCurrentUser проверка eventId = {}", eventId);

        return eventService.updateChangingTheStatusOfApplicationsForParticipationInAnEventForTheCurrentUser(eventRequestStatusUpdateRequest, userId, eventId);
    }
}
