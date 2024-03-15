package ru.practicum.main.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.evente.Event;
import ru.practicum.main.evente.EventRepository;
import ru.practicum.main.evente.StatusType;
import ru.practicum.main.exception.NotFoundExceptionConflict;
import ru.practicum.main.request.dto.RequestDto;
import ru.practicum.main.users.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Slf4j
@Service
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public RequestServiceImpl(RequestRepository requestRepository, UserRepository userRepository, EventRepository eventRepository) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    @Transactional
    @Override
    public RequestDto createRrequest(long userId, long eventId) {

        checkUserId(userId);//проверка id user на существование
        checkEventId(eventId);//проверка id event на существование
        checkEventInitiatorId(userId, eventId);//проверка на инициатора события
        checkEventStats(eventId);//проверка собыитя на PUBLISHED
        Event event = eventRepository.findById(eventId).get();
        Request request = new Request();

        if (event.getConfirmedRequests() >= event.getParticipantLimit() && event.getParticipantLimit() != 0) {
            throw new NotFoundExceptionConflict(String.format("Event ConfirmedRequests = {} достигнут лимита = {} !", event.getConfirmedRequests(),
                    event.getParticipantLimit()));
        }

        if (!event.isRequestModeration() || event.getParticipantLimit() == 0) {
            request = Request.builder()
                    .created(LocalDateTime.now())
                    .event(eventRepository.findById(eventId).get())
                    .requester(userRepository.findById(userId).get())
                    .status(String.valueOf(StatusType.CONFIRMED))
                    .build();
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        } else {
            request = Request.builder()
                    .created(LocalDateTime.now())
                    .event(eventRepository.findById(eventId).get())
                    .requester(userRepository.findById(userId).get())
                    .status(String.valueOf(StatusType.PENDING))
                    .build();
        }

        request = requestRepository.save(request);
        return RequestMapper.toRequestDto(request);
    }

    @Transactional
    @Override
    public RequestDto updateRrequest(long userId, long requestId) {
        checkUserId(userId);//проверка id user на существование
        checkRequestId(requestId);//проврка id request на существование

        Request request = requestRepository.findById(requestId).get();
        request.setStatus(String.valueOf(StatusType.CANCELED));

        request = requestRepository.save(request);
        return RequestMapper.toRequestDto(request);
    }

    @Override
    public List<RequestDto> getObtainingInformationAboutTheCurrentUserRequestsToParticipateInOtherPeoplesEvents(long userId) {
        log.info("Сервис метод getObtainingInformationAboutTheCurrentUserRequestsToParticipateInOtherPeoplesEvents проверка userId = {}", userId);
        checkUserId(userId);
        List<Request> requests = requestRepository.findAllByRequester_Id(userId);
        log.info("Сервис метод getObtainingInformationAboutTheCurrentUserRequestsToParticipateInOtherPeoplesEvents проверка requests = {}", requests);
        return requests.stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    private void checkEventInitiatorId(long userId, long eventId) {
        log.info("Проверка сервис метод checkEventInitiator проверка на создание запроса инициатором события");
        log.info("Проверка события к бд {}", eventRepository.findById(eventId).get());
        if (eventRepository.findById(eventId).get().getInitiator().getId() == userId) {
            throw new NotFoundExceptionConflict(String.format("User с таким id = %s  является создателем события!", userId));
        }
    }

    private void checkEventStats(long eventId) {
        log.info("Проверка сервис метод checkEventStats проверка статуса события на PUBLISHED");
        log.info("Проверка события к бд {}", eventRepository.findById(eventId).get().getState());
        if (!eventRepository.findById(eventId).get().getState().equals(String.valueOf(StatusType.PUBLISHED))) {
            throw new NotFoundExceptionConflict(String.format("Event с таким id = %s имеет статус = %s!", eventId,
                    eventRepository.findById(eventId).get().getState()));
        }
    }

    private void checkUserId(long userId) {
        log.info("Проверка сервис метод checkUserId проверка Id User на существование");
        log.info("Проверка события к бд {}", userRepository.existsById(userId));
        if (!userRepository.existsById(userId)) {
            throw new NotFoundExceptionConflict(String.format("User с таким id = %s не сущетсвует!", userId));
        }
    }

    private void checkEventId(long eventId) {
        log.info("Проверка сервис метод checkEventId проверка Id Event на существование");
        log.info("Проверка события к бд {}", eventRepository.existsById(eventId));
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundExceptionConflict(String.format("Event с таким id = %s не сущетсвует!", eventId));
        }
    }

    private void checkRequestId(long requestId) {
        log.info("Проверка сервис метод checkRequestId проверка Id Event на существование");
        log.info("Проверка события к бд {}", requestRepository.existsById(requestId));
        if (!requestRepository.existsById(requestId)) {
            throw new NotFoundExceptionConflict(String.format("Request с таким id = %s не сущетсвует!", requestId));
        }
    }


}
