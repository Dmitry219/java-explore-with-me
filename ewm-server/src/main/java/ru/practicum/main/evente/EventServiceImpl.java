package ru.practicum.main.evente;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.practicum.RequestStatsDto;
import ru.practicum.ResponseStatsDto;
import ru.practicum.StatsClient;
import ru.practicum.main.QueryEvent.AdminEventFilterRequest;
import ru.practicum.main.QueryEvent.PublicEventFilterRequest;
import ru.practicum.main.category.CategoryRepository;

import ru.practicum.main.comments.*;
import ru.practicum.main.evente.dto.*;
import ru.practicum.main.evente.dto.EventRequestStatusUpdateResult;
import ru.practicum.main.exception.Event.AuthorizationFailureExceptionEvent;
import ru.practicum.main.exception.Event.NotFoundExceptionConflictEvent;
import ru.practicum.main.exception.NotFoundExceptionConflict;
import ru.practicum.main.exception.User.NotFoundExceptionUser;
import ru.practicum.main.exception.User.NullPointerExceptionpUser;
import ru.practicum.main.exception.ValidationUserException;
import ru.practicum.main.request.Request;
import ru.practicum.main.request.RequestMapper;
import ru.practicum.main.request.RequestRepository;
import ru.practicum.main.request.dto.RequestDto;
import ru.practicum.main.users.User;
import ru.practicum.main.users.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import java.util.List;

import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@Slf4j
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final CommentRepository commentRepository;
    private final StatsClient statsClient;
    private final ObjectMapper objectMapper;

    public EventServiceImpl(EventRepository eventRepository, CategoryRepository categoryRepository,
                            UserRepository userRepository, RequestRepository requestRepository, CommentRepository commentRepository,
                            StatsClient statsClient, ObjectMapper objectMapper) {
        this.eventRepository = eventRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.requestRepository = requestRepository;
        this.commentRepository = commentRepository;
        this.statsClient = statsClient;
        this.objectMapper = objectMapper;
    }

    @Transactional
    @Override
    public EventDtoResponse createEvent(EventDtoRequest eventDtoRequest, long userId) {
        log.info("Сервис createEvent проверка eventDtoRequest = {}", eventDtoRequest);
        log.info("Сервис createEvent проверка userId = {}", userId);
        checkUser(userId);
        checkCategory(eventDtoRequest.getCategory());
        Event newEvent = EventMapping.toEvent(eventDtoRequest);
        newEvent.setState(String.valueOf(StatusType.PENDING));

        newEvent.setCategory(categoryRepository.findById(eventDtoRequest.getCategory()).get());
        newEvent.setInitiator(userRepository.findById(userId).get());
        newEvent.setCreatedOn(LocalDateTime.now());
        log.info("Сервис createEvent проверка newEvent = {}", newEvent);

        Event saveEvent = eventRepository.save(newEvent);
        log.info("Сервис createEvent проверка сохранения saveEvent = {}", saveEvent);

        checkTime(saveEvent, eventDtoRequest.getEventDate());

        EventDtoResponse eventDtoResponse = EventMapping.toEventDtoResponse(saveEvent, new ArrayList<>());
        log.info("Сервис createEvent проверка маппинга eventDtoResponse = {}", eventDtoResponse);

        return eventDtoResponse;
    }

    @Override
    public List<EventShortDto> getReceivingEventsAddedByTheCurrentUser(long userId, int from, int size) {
        log.info("Сервис getReceivingEventsAddedByTheCurrentUser проверка userId = {}", userId);
        log.info("Сервис getReceivingEventsAddedByTheCurrentUser проверка from = {}", from);
        log.info("Сервис getReceivingEventsAddedByTheCurrentUser проверка size = {}", size);
        checkUser(userId);
        checkInitiatorId(userId);
        PageRequest pageRequest = PageRequest.of(from,size);
        List<Event> events = new ArrayList<>();
        events = eventRepository.findAllByInitiatorId(userId, pageRequest);
        log.info("Сервис getReceivingEventsAddedByTheCurrentUser проверка events = {}", events);

        return events.stream()
                .map(EventMapping::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventDtoResponse getObtainingCompleteInformationAboutTheEventByTheAddedCurrentUser(long userId, long eventId) {
        log.info("Сервис getObtainingCompleteInformationAboutTheEventByTheAddedCurrentUser проверка userId = {}", userId);
        log.info("Сервис getObtainingCompleteInformationAboutTheEventByTheAddedCurrentUser проверка eventId = {}", eventId);
        checkUser(userId);
        checkEvent(eventId);
        checkOwnerEvent(userId, eventId);
        Event event = eventRepository.findByInitiatorIdAndId(userId, eventId);
        log.info("Сервис getObtainingCompleteInformationAboutTheEventByTheAddedCurrentUser проверка event = {}", event);

        List<Comment> comments = new ArrayList<>();
        List<CommentDtoShort> commentDtos = new ArrayList<>();
        comments = commentRepository.findAllByEventId(event.getId());
        if (comments != null) {
            commentDtos = comments.stream()
                    .map(CommentMapping::toCommentDtoShort)
                    .collect(Collectors.toList());
        }

        return EventMapping.toEventDtoResponse(event, commentDtos);
    }

    @Transactional
    @Override
    public EventDtoResponse updatingAnEventAddedByTheCurrentUser(EventDtoRequestUpdateStateAction eventDtoRequestUpdateStateAction,
                                                                 long userId, long eventId) {
        log.info("Сервис updatingAnEventAddedByTheCurrentUser проверка eventDtoRequestUpdateStateAction = {}", eventDtoRequestUpdateStateAction);
        log.info("Сервис updatingAnEventAddedByTheCurrentUser проверка userId = {}", userId);
        log.info("Сервис updatingAnEventAddedByTheCurrentUser проверка eventId = {}", eventId);
        checkUser(userId);
        checkEvent(eventId);
        checkOwnerEvent(userId, eventId);


        Event eventUpdate = eventRepository.findByInitiatorIdAndId(userId, eventId);

        if (!eventUpdate.getState().equals(String.valueOf(StatusType.PUBLISHED))) {

            if (eventDtoRequestUpdateStateAction.getPaid() != null) {
                eventUpdate.setPaid(eventDtoRequestUpdateStateAction.getPaid());
            }

            if (eventDtoRequestUpdateStateAction.getRequestModeration() != null) {
                eventUpdate.setRequestModeration(eventDtoRequestUpdateStateAction.getRequestModeration());
            }

            if (eventDtoRequestUpdateStateAction.getAnnotation() != null) {
                eventUpdate.setAnnotation(eventDtoRequestUpdateStateAction.getAnnotation());
            }
            if (eventDtoRequestUpdateStateAction.getCategory() != null) {
                eventUpdate.setCategory(categoryRepository.findById(eventDtoRequestUpdateStateAction.getCategory()).get());
            }
            if (eventDtoRequestUpdateStateAction.getDescription() != null) {
                eventUpdate.setDescription(eventDtoRequestUpdateStateAction.getDescription());
            }
            if (eventDtoRequestUpdateStateAction.getEventDate() != null) {
                checkTime(eventUpdate, eventDtoRequestUpdateStateAction.getEventDate());

                if (LocalDateTime.now().isAfter(eventDtoRequestUpdateStateAction.getEventDate().minusHours(2L))) {
                    throw new NotFoundExceptionConflict(String.format("eventDate = %s раньше чем текущее = %s", eventDtoRequestUpdateStateAction.getEventDate(),
                            LocalDateTime.now()));
                }
                eventUpdate.setEventDate(eventDtoRequestUpdateStateAction.getEventDate());
            }
            if (eventDtoRequestUpdateStateAction.getLocation() != null) {
                eventUpdate.setLocation(eventDtoRequestUpdateStateAction.getLocation());
            }
            if (eventDtoRequestUpdateStateAction.getParticipantLimit() != null) {
                eventUpdate.setParticipantLimit(eventDtoRequestUpdateStateAction.getParticipantLimit());
            }
            if (eventDtoRequestUpdateStateAction.getTitle() != null) {
                eventUpdate.setTitle(eventDtoRequestUpdateStateAction.getTitle());
            }

            if (eventDtoRequestUpdateStateAction.getStateAction() != null) {
                if (eventDtoRequestUpdateStateAction.getStateAction().equals(StateAction.SEND_TO_REVIEW)) {
                    eventUpdate.setState(String.valueOf(StatusType.PENDING));
                } else if (eventDtoRequestUpdateStateAction.getStateAction().equals(StateAction.CANCEL_REVIEW)) {
                    eventUpdate.setState(String.valueOf(StatusType.CANCELED));
                } else {
                    eventUpdate.setState(String.valueOf(eventDtoRequestUpdateStateAction.getStateAction()));
                }
            }
        } else {
            throw new NotFoundExceptionConflictEvent("Only pending or canceled events can be changed");
        }

        Event updateEventSave = eventRepository.save(eventUpdate);

        List<Comment> comments = new ArrayList<>();
        List<CommentDtoShort> commentDtos = new ArrayList<>();
        comments = commentRepository.findAllByEventId(eventUpdate.getId());
        if (comments != null) {
            commentDtos = comments.stream()
                    .map(CommentMapping::toCommentDtoShort)
                    .collect(Collectors.toList());
        }

        return EventMapping.toEventDtoResponse(updateEventSave, commentDtos);
    }

    @Transactional
    @Override
    public EventDtoResponse getObtainingDetailedInformationAboutAPublishedEventByItsId(long id, HttpServletRequest request) {

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

        log.info("Public сервис метод getObtainingDetailedInformationAboutAPublishedEventByItsId проверка id = {}", id);
        Event event = eventRepository.findById(id).get();
        event.setViews(viwes.get(0).getHits());//положили количество просмотров
        Event updateEvent = eventRepository.save(event);//сохранили событие с просмотрами
        if (!event.getState().equals(String.valueOf(StatusType.PUBLISHED))) {
            throw new NotFoundExceptionUser(String.format("Событие не опубликовано!"));
        }

        log.info("Public сервис метод getObtainingDetailedInformationAboutAPublishedEventByItsId проверка получение из базы event = {}", event);

        List<Comment> comments = new ArrayList<>();
        List<CommentDtoShort> commentDtos = new ArrayList<>();
        comments = commentRepository.findAllByEventId(id);

        if (comments != null) {
            commentDtos = comments.stream()
                    .map(CommentMapping::toCommentDtoShort)
                    .collect(Collectors.toList());
        }

        return EventMapping.toEventDtoResponse(event, commentDtos);
    }

    @Override
    public List<RequestDto> getObtainingInformationAboutRequestsToParticipateInAnEventOfTheCurrentUser(long userId,
                                                                                                       long eventId) {
        log.info("Private сервис метод getObtainingInformationAboutRequestsToParticipateInAnEventOfTheCurrentUser проверка userId = {}", userId);
        log.info("Private сервис метод getObtainingInformationAboutRequestsToParticipateInAnEventOfTheCurrentUser проверка eventId = {}", eventId);

        checkEvent(eventId);
        checkUser(userId);
        checkOwnerEvent(userId, eventId);
        List<Request> requests = new ArrayList<>();

        Event event = eventRepository.findByInitiatorIdAndId(userId,eventId);
        log.info("Private сервис метод getObtainingInformationAboutRequestsToParticipateInAnEventOfTheCurrentUser проверка event = {}", event);

        requests = requestRepository.findByEvent_Id(eventId);
        log.info("Private сервис метод getObtainingInformationAboutRequestsToParticipateInAnEventOfTheCurrentUser проверка requests = {}", requests);

        return requests.stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventDtoResponse> searchForAnEvent(AdminEventFilterRequest adminEventFilterRequest) {
        List<Event> events = new ArrayList<>();
        validateTime(adminEventFilterRequest.getRangeStart(), adminEventFilterRequest.getRangeEnd());

        QEvent qEvent = QEvent.event;
        List<BooleanExpression> predicates = new ArrayList<>();
        PageRequest pageRequest = PageRequest.of(adminEventFilterRequest.getFrom() / adminEventFilterRequest.getSize(),
                adminEventFilterRequest.getSize());

        if (adminEventFilterRequest.getUsers() != null) {
            predicates.add(
                    qEvent.initiator.id.in(adminEventFilterRequest.getUsers())
            );
        }
        if (adminEventFilterRequest.getStates() != null) {
            predicates.add(
                    qEvent.state.in(adminEventFilterRequest.getStates().stream()
                            .map(StatusType::name)
                            .collect(Collectors.toList()))
            );
        }
        if (adminEventFilterRequest.getCategories() != null) {
            predicates.add(
                    qEvent.category.id.in(adminEventFilterRequest.getCategories())
            );
        }
        if (adminEventFilterRequest.getRangeStart() != null) {
            predicates.add(
                    qEvent.eventDate.goe(adminEventFilterRequest.getRangeStart())
            );
        }
        if (adminEventFilterRequest.getRangeEnd() != null) {
            predicates.add(
                    qEvent.eventDate.loe(adminEventFilterRequest.getRangeEnd())
            );
        }

        Predicate predicate = predicates.stream()
                .reduce(BooleanExpression::and)
                .orElse(null);

        if (predicate != null) {
            events = eventRepository.findAll(predicate, pageRequest).getContent();
        } else {
            events = eventRepository.findAll(pageRequest).getContent();
        }
        log.info("Admin сервис проверка events {}", events);

        return events.stream()
                .map(event ->
                        EventMapping.toEventDtoResponse(event, commentRepository.findAllByEventId(event.getId())
                                .stream()
                                .map(CommentMapping::toCommentDtoShort)
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

//    1 это публичный эндпоинт, соответственно в выдаче должны быть только опубликованные события
//    2 текстовый поиск (по аннотации и подробному описанию) должен быть без учета регистра букв
//    3 если в запросе не указан диапазон дат [rangeStart-rangeEnd], то нужно выгружать события, которые произойдут позже текущей даты и времени
//    4 информация о каждом событии должна включать в себя количество просмотров и количество уже одобренных заявок на участие
//    5 информацию о том, что по этому эндпоинту был осуществлен и обработан запрос, нужно сохранить в сервисе статистики*/
    @Override
    public List<EventShortDto> getEvents(PublicEventFilterRequest publicEventFilterRequest, HttpServletRequest request) {

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

        List<Event> events = new ArrayList<>();
        QEvent qEvent = QEvent.event;
        List<BooleanExpression> predicates = new ArrayList<>();
        predicates.add(qEvent.state.eq(String.valueOf(StatusType.PUBLISHED)));
        PageRequest pageRequest = PageRequest.of(publicEventFilterRequest.getFrom() / publicEventFilterRequest.getSize(),
                publicEventFilterRequest.getSize());

        if (publicEventFilterRequest.getText() != null) {
            predicates.add(
                    qEvent.annotation.containsIgnoreCase(publicEventFilterRequest.getText())
                    .or(qEvent.description.containsIgnoreCase(publicEventFilterRequest.getText())));

        }
        if (publicEventFilterRequest.getCategories() != null) {
            predicates.add(
                    qEvent.category.id.in(publicEventFilterRequest.getCategories())
            );
        }
        if (publicEventFilterRequest.getPaid() != null) {
            predicates.add(
                    qEvent.paid.in(publicEventFilterRequest.getPaid())
            );
        }
        if (publicEventFilterRequest.getRangeStart() != null && publicEventFilterRequest.getRangeEnd() != null) {
            validateTime(publicEventFilterRequest.getRangeStart(), publicEventFilterRequest.getRangeEnd());
            predicates.add(
                    qEvent.eventDate.between(publicEventFilterRequest.getRangeStart(), publicEventFilterRequest.getRangeEnd())
            );
        } else {
            qEvent.eventDate.after(LocalDateTime.now());
        }

        Predicate predicate = predicates.stream()
                .reduce(BooleanExpression::and)
                .orElse(null);

        if (predicate != null) {
            events = eventRepository.findAll(predicate, pageRequest).getContent();
        } else {
            events = eventRepository.findAll(pageRequest).getContent();
        }

        return events.stream()
                .filter(event -> {
                    if (publicEventFilterRequest.getOnlyAvailable() && event.getParticipantLimit() > 0) {
                        return event.isPaid();
                    }
                    return true;
                })
                .map(EventMapping::toEventShortDto)
                .collect(Collectors.toList());
    }

    private void validateTime(LocalDateTime start, LocalDateTime end) {
        if (start != null && end != null && start.isAfter(end)) {
            throw new IllegalArgumentException("Старт после конца!");
        }
    }

    @Transactional
    @Override
    public EventDtoResponse updatingEventDataAndStatus(EventDtoRequestUpdateStateAction eventDtoRequestUpdateStateAction, long eventId) {
        log.info("Сервис updatingEventDataAndStatus проверка eventDtoRequestUpdateStateAction = {}", eventDtoRequestUpdateStateAction);
        log.info("Сервис updatingEventDataAndStatus проверка eventId = {}", eventId);

        Event eventUpdate = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundExceptionConflict(String.format("Event with id=2 was not found", eventId)));

        checkEventDateAndNow(eventUpdate); //проверка что события находится на час после начала

        if (eventDtoRequestUpdateStateAction.getPaid() != null) {
            eventUpdate.setPaid(eventDtoRequestUpdateStateAction.getPaid());
        }

        if (eventDtoRequestUpdateStateAction.getRequestModeration() != null) {
            eventUpdate.setRequestModeration(eventDtoRequestUpdateStateAction.getRequestModeration());
        }

        if (eventDtoRequestUpdateStateAction.getAnnotation() != null) {
            eventUpdate.setAnnotation(eventDtoRequestUpdateStateAction.getAnnotation());
        }
        if (eventDtoRequestUpdateStateAction.getCategory() != null) {
            eventUpdate.setCategory(categoryRepository.findById(eventDtoRequestUpdateStateAction.getCategory()).get());
        }
        if (eventDtoRequestUpdateStateAction.getDescription() != null) {
            eventUpdate.setDescription(eventDtoRequestUpdateStateAction.getDescription());
        }
        if (eventDtoRequestUpdateStateAction.getEventDate() != null) {
            checkTime(eventUpdate, eventDtoRequestUpdateStateAction.getEventDate());
            eventUpdate.setEventDate(eventDtoRequestUpdateStateAction.getEventDate());
        }
        if (eventDtoRequestUpdateStateAction.getLocation() != null) {
            eventUpdate.setLocation(eventDtoRequestUpdateStateAction.getLocation());
        }
        if (eventDtoRequestUpdateStateAction.getParticipantLimit() != null) {
            eventUpdate.setParticipantLimit(eventDtoRequestUpdateStateAction.getParticipantLimit());
        }
        if (eventDtoRequestUpdateStateAction.getTitle() != null) {
            eventUpdate.setTitle(eventDtoRequestUpdateStateAction.getTitle());
        }
        if (eventDtoRequestUpdateStateAction.getStateAction() != null) {
            if (!eventUpdate.getState().equals(String.valueOf(StatusType.PUBLISHED))) {
                if (eventUpdate.getState().equals(String.valueOf(StatusType.PENDING))) {
                    if (eventDtoRequestUpdateStateAction.getStateAction().equals(StateAction.PUBLISH_EVENT)) {
                        eventUpdate.setState(String.valueOf(StatusType.PUBLISHED));
                        eventUpdate.setPublishedOn(LocalDateTime.now());
                        checkTimePublished(eventUpdate.getPublishedOn(), eventUpdate.getEventDate());
                    } else if (eventDtoRequestUpdateStateAction.getStateAction().equals(StateAction.REJECT_EVENT)) {
                        eventUpdate.setState(String.valueOf(StatusType.CANCELED));
                    }
                } else {
                    throw new NotFoundExceptionConflict(String.format("У Событя статус отклонено и его статутс нельзя изменить"));
                }
            } else {
                throw new NotFoundExceptionConflict(String.format("У События статус опубликовано и его статутс нельзя изменить"));
            }
        }

        log.info("Вот что получилсоь eventUpdate = {}", eventUpdate);

        List<Comment> comments = new ArrayList<>();
        List<CommentDtoShort> commentDtos = new ArrayList<>();

        comments = commentRepository.findAllByEventId(eventUpdate.getId());

        if (comments != null) {
            commentDtos = comments.stream()
                    .map(CommentMapping::toCommentDtoShort)
                    .collect(Collectors.toList());
        }

        return EventMapping.toEventDtoResponse(eventRepository.save(eventUpdate), commentDtos);
    }

    @Transactional
    @Override
    public EventRequestStatusUpdateResult updateChangingTheStatusOfApplicationsForParticipationInAnEventForTheCurrentUser(EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest,
                                                                                                                          long userId,
                                                                                                                          long eventId) {
        checkUser(userId);
        checkEvent(eventId);
        Event event = eventRepository.findById(eventId).get();
        checkOwnerEvent(userId, eventId);

        List<Request> requests = requestRepository.findAllByIdIn(eventRequestStatusUpdateRequest.getRequestIds());

        List<Request> updateRequestsConfirmed = new ArrayList<>();
        List<Request> updateRequestsRejected = new ArrayList<>();

        //если лимит исчерпан
        if (event.getParticipantLimit() == event.getConfirmedRequests()) {
            throw new NotFoundExceptionConflictEvent("The participant limit has been reached");
        }

        //если из списка хотя бы одна заявка не в ожидании то выбрасываем исключение
        requests.forEach(request -> {
            if (!request.getStatus().equals(String.valueOf(StatusType.PENDING))) {
            throw new NullPointerExceptionpUser("Request must have status PENDING");
            }
        });

        //есть несколько свободных мест
        if (eventRequestStatusUpdateRequest.getStatus().equals(StatusType.CONFIRMED)) {
            if (event.getParticipantLimit() - event.getConfirmedRequests() > 0) {
                long numberFreePlaces = event.getParticipantLimit() - event.getConfirmedRequests();//кол-во свободных мест
                long numberOfRequest = eventRequestStatusUpdateRequest.getRequestIds().size();//кол-во нужных мест

                 if ((numberFreePlaces - numberOfRequest) >= 0) { //мест хватит на всех
                    updateRequestsConfirmed = requests.stream()
                            .peek(request -> request.setStatus(String.valueOf(StatusType.CONFIRMED)))
                            .collect(Collectors.toList());

                    updateRequestsRejected = requests.stream()
                            .filter(request -> request.getStatus().equals(String.valueOf(StatusType.REJECTED)))
                            .collect(Collectors.toList());

                } else if ((numberFreePlaces - numberOfRequest) < 0) { //мест хватит не на всех
                    long numberOfExtra = -(numberFreePlaces - numberOfRequest);//количество лишних запросов и делаем из отрицательного числа положительное
                    List<Long> subListConf = eventRequestStatusUpdateRequest.getRequestIds()
                            .subList(0, (int) (eventRequestStatusUpdateRequest.getRequestIds().size() - numberOfExtra));
                    List<Request> requestsSubConf = requestRepository.findAllByIdInAndStatus(subListConf, String.valueOf(StatusType.PENDING));
                    updateRequestsConfirmed = requestsSubConf.stream()
                            .peek(request -> request.setStatus(String.valueOf(StatusType.CONFIRMED)))
                            .collect(Collectors.toList());

                    List<Long> subListRej = eventRequestStatusUpdateRequest.getRequestIds()
                            .subList((int) (eventRequestStatusUpdateRequest.getRequestIds().size() - numberOfExtra), eventRequestStatusUpdateRequest.getRequestIds().size());
                    List<Request> requestsSubRej = requestRepository.findAllByIdInAndStatus(subListRej, String.valueOf(StatusType.PENDING));
                    updateRequestsRejected = requestsSubRej.stream()
                            .peek(request -> request.setStatus(String.valueOf(StatusType.REJECTED)))
                            .collect(Collectors.toList());
                }
            }
        } else {
            updateRequestsRejected = requests.stream()
                    .peek(request -> request.setStatus(String.valueOf(StatusType.REJECTED)))
                    .collect(Collectors.toList());
        }

        log.info("updateRequestsConfirmed = {}", updateRequestsConfirmed);
        log.info("updateRequestsRejected = {}", updateRequestsRejected);
        event.setConfirmedRequests(event.getConfirmedRequests() + updateRequestsConfirmed.size());
        eventRepository.save(event);

        List<RequestDto> updateConfirmed = requestRepository.saveAll(updateRequestsConfirmed).stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
        List<RequestDto> updateRejected = requestRepository.saveAll(updateRequestsRejected).stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());

        return new EventRequestStatusUpdateResult(updateConfirmed, updateRejected);

    }

    //---------------------------------Работа с комментированием------
    @Transactional
    @Override
    public CommentDto createComment(CommentDto commentDto, long userId, long eventId) {
        log.info("Проверка сервис метод createComment проверка commentDto={} , userId={} , eventId={} - ", commentDto, userId, eventId);
        checkUser(userId);
        checkEvent(eventId);
        checkInitiatorId(userId);

        User user = userRepository.findById(userId).get();
        Event event = eventRepository.findByIdAndState(eventId, String.valueOf(StatusType.PUBLISHED));

        if (event.getInitiator().getId() == userId) {
            throw new RuntimeException("Создатель не может оставлять комментарии!");
        }

        log.info("Проверка сервис метод createComment проверка event={}", event);

        Comment comment = null;
        CommentDto commentDtoNew = null;

        if (event != null) {
            comment = CommentMapping.toComment(commentDto, event, user);
            commentDtoNew = CommentMapping.toCommentDto(commentRepository.save(comment));
        } else {
            throw new ValidationUserException("В базе отсутствует событие!");
        }

        return commentDtoNew;
    }

    @Transactional
    @Override
    public CommentDto updateComment(CommentDto commentDto, long userId, long commitId) {
        log.info("Проверка сервис метод updateComment проверка commentDto={} , userId={} , commitId={} - ",
                commentDto, userId, commitId);
        checkUser(userId);

        User user = userRepository.findById(userId).get();
        Comment comment = commentRepository.findById(commitId).get();
        Event event = eventRepository.findById(comment.getEvent().getId()).get();

        if (comment.getAuthor() != user) {
            throw new RuntimeException("Пользователь не создатель коммента, только владелец может изменить комментарий !");
        }
        if (!event.getState().equals(String.valueOf(StatusType.PUBLISHED))) {
            throw new NotFoundExceptionConflict("Событие не опубликовано и его комментарии нельзя изменить !");
        }

        if (commentDto.getText() != null) {
            comment.setText(commentDto.getText());
        }

        CommentDto commentDtoNew = null;
        commentDtoNew = CommentMapping.toCommentDto(commentRepository.save(comment));

        return commentDtoNew;
    }

    @Transactional
    @Override
    public void deleteCommentById(long commitId) {
        if (!commentRepository.existsById(commitId)) {
            throw new NotFoundExceptionConflict("Не существует комментарий!");
        }
        commentRepository.deleteById(commitId);
    }

    @Override
    public CommentDtoShort getByIdComment(long commitId) {
        log.info("Проверка сервис метод getBuIdComment проверка commitId={} ", commitId);
        if (!commentRepository.existsById(commitId)) {
            throw new NotFoundExceptionConflict("Не существует комментарий!");
        }

        Comment comment = commentRepository.findById(commitId).get();
        log.info("Проверка сервис метод getBuIdComment проверка comment={}",
                comment);
        return CommentMapping.toCommentDtoShort(comment);
    }

    @Override
    public List<CommentDtoShort> getAllByComment(long userId) {
        log.info("Проверка сервис метод getBuIdComment проверка userId={}", userId);

        checkUser(userId);
        List<Comment> comments = commentRepository.findAllByAuthorId(userId);
        if (comments != null) {
            return comments.stream().map(CommentMapping::toCommentDtoShort).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public List<CommentDtoShort> getAllBySearcheComment(String text) {
        List<Comment> comments = new ArrayList<>();
        List<CommentDtoShort> commentDtos = new ArrayList<>();
        if (text != null) {
            //comments = commentRepository.findAllByTextContaining(text);
            comments = commentRepository.findAllByText(text.toLowerCase());
            return comments.stream().map(CommentMapping::toCommentDtoShort).collect(Collectors.toList());
        } else {
            return commentDtos;
        }
    }

    public List<CommentDtoShort> getAllComments() {
        return commentRepository.findAll().stream()
                .map(CommentMapping::toCommentDtoShort)
                .collect(Collectors.toList());
    }

    //--------------------------------------------------------------

    private void checkTime(Event event, LocalDateTime eventDate) {
        log.info("Проверка сервис метод checkTime проверка eventDate должно быть впереди текущего");

        if (event.getCreatedOn().isAfter(eventDate.minusHours(2L))) {
            throw new AuthorizationFailureExceptionEvent(String.format("Field: eventDate. Error: должно содержать дату, которая еще не наступила. Value: %s", eventDate));
        }
    }

    private void checkEventDateAndNow(Event event) {
        if (ChronoUnit.HOURS.between(LocalDateTime.now(), event.getEventDate()) < 1) {
            throw new NotFoundExceptionConflict(String.format("eventDate = %s раньше чем за час до начала публикации = %s", event.getEventDate().minusHours(1L), LocalDateTime.now()));
        }
    }

    private void checkTimePublished(LocalDateTime publishedOn, LocalDateTime eventDate) {
        log.info("Проверка сервис метод checkTimePublished проверка дата публикации должна быть раньше на час даты события");

        if (publishedOn.isAfter(eventDate.minusHours(1L))) {
            throw new NotFoundExceptionConflict(String.format("eventDate = %s раньше чем publishedOn = %s", eventDate, publishedOn));
        }
    }

    private void checkUser(long userId) {
        log.info("Проверка сервис метод checkUser проверка ID USER в");
        if (!userRepository.existsById(userId)) {
            throw new NotFoundExceptionConflict(String.format("User с таким id = %s  не существует!", userId));
        }
    }

    private void checkCategory(long catId) {
        log.info("Проверка сервис метод checkCategory проверка ID Category в");
        if (!categoryRepository.existsById(catId)) {
            throw new NullPointerExceptionpUser("Field: category. Error: must not be blank. Value: null\"");
        }
    }


    private void checkEvent(long eventId) {
        log.info("Проверка сервис метод checkOwnerEvent проверка ID Event в");
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundExceptionUser(String.format("Event with id=%s was not found", eventId));
        }
    }

    private void checkOwnerEvent(long userId, long eventId) {
        log.info("Проверка сервис метод checkOwnerEvent проверка ID USER является владельцем события");
        if (eventRepository.findById(eventId).get().getInitiator().getId() != userId) {
            throw new NotFoundExceptionConflict(String.format("User с таким id = %s  не создатель события!", userId));
        }
    }

    private void checkInitiatorId(long userId) {
        log.info("Проверка сервис метод checkInitiatorId проверка ID USER является владельцем события");
        if (eventRepository.findAllByInitiatorId(userId) == null) {
            throw new NotFoundExceptionConflict(String.format("User с таким id = %s  не создовал события!", userId));
        }
    }

}
