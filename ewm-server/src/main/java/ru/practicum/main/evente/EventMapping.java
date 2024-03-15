package ru.practicum.main.evente;

import lombok.experimental.UtilityClass;
import ru.practicum.main.category.CategoryMapper;
import ru.practicum.main.evente.dto.EventDtoRequest;
import ru.practicum.main.evente.dto.EventDtoRequestUpdateStateAction;
import ru.practicum.main.evente.dto.EventDtoResponse;
import ru.practicum.main.evente.dto.EventShortDto;
import ru.practicum.main.users.UserMapper;

@UtilityClass
public class EventMapping {
    public Event toEvent(EventDtoRequest eventDtoRequest) {
        return Event.builder()
                .annotation(eventDtoRequest.getAnnotation())
                .description(eventDtoRequest.getDescription())
                .eventDate(eventDtoRequest.getEventDate())
                .location(eventDtoRequest.getLocation())
                .paid(eventDtoRequest.isPaid())
                .participantLimit(eventDtoRequest.getParticipantLimit())
                .requestModeration(eventDtoRequest.isRequestModeration())
                .title(eventDtoRequest.getTitle())
                .build();
    }

    public EventDtoResponse toEventDtoResponse(Event event) {
        return EventDtoResponse.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .initiator(UserMapper.toUserDtoResponseEvent(event.getInitiator()))
                .location(event.getLocation())
                .paid(event.isPaid())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.isRequestModeration())
                .state(StatusType.valueOf(event.getState()))
                .title(event.getTitle())
                .createdOn(event.getCreatedOn())
                .publishedOn(event.getPublishedOn())
                .confirmedRequests(event.getConfirmedRequests())
                .views(event.getViews())
                .build();
    }

    public EventShortDto toEventShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .eventDate(event.getEventDate())
                .initiator(UserMapper.toUserDtoResponseEvent(event.getInitiator()))
                .paid(event.isPaid())
                .title(event.getTitle())
                .confirmedRequests(event.getConfirmedRequests())
                .build();
    }
}
