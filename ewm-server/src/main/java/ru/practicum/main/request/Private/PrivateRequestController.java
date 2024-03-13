package ru.practicum.main.request.Private;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.request.RequestService;
import ru.practicum.main.request.dto.RequestDto;

import java.util.List;

@RestController
@Slf4j
public class PrivateRequestController {
    private final RequestService requestService;

    public PrivateRequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    //добавление запроса от текущего пользователя на участие в событии
    @PostMapping("/users/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDto createRrequest(@PathVariable long userId, @RequestParam long eventId) {
        log.info("Контроллер метод createRrequest проверка userId = {}", userId);
        log.info("Контроллер метод createRrequest проверка eventId = {}", eventId);
        return requestService.createRrequest(userId, eventId);
    }

    //отмена своего запроса на участие в событии
    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    public RequestDto updateRrequest(@PathVariable long userId, @PathVariable long requestId) {
        log.info("Контроллер метод updateRrequest проверка userId = {}", userId);
        log.info("Контроллер метод updateRrequest проверка requestId = {}", requestId);
        return requestService.updateRrequest(userId, requestId);
    }

    //Получение информации о заявках текущего пользователя на участие в чужих событиях
    @GetMapping("/users/{userId}/requests")
    public List<RequestDto> getObtainingInformationAboutTheCurrentUserRequestsToParticipateInOtherPeoplesEvents(@PathVariable long userId) {
        log.info("Контроллер метод getObtainingInformationAboutTheCurrentUserRequestsToParticipateInOtherPeoplesEvents проверка from = {}", userId);
        return requestService.getObtainingInformationAboutTheCurrentUserRequestsToParticipateInOtherPeoplesEvents(userId);
    }
}
