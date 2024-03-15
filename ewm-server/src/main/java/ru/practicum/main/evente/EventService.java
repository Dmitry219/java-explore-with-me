package ru.practicum.main.evente;

import ru.practicum.main.QueryEvent.AdminEventFilterRequest;
import ru.practicum.main.QueryEvent.PublicEventFilterRequest;
import ru.practicum.main.evente.dto.*;
import ru.practicum.main.request.dto.RequestDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {
    EventDtoResponse createEvent(EventDtoRequest eventDtoRequest, long userId);

    List<EventShortDto> getReceivingEventsAddedByTheCurrentUser(long userId, int from, int size);

    EventDtoResponse getObtainingCompleteInformationAboutTheEventByTheAddedCurrentUser(long userId, long eventId);

    EventDtoResponse updatingAnEventAddedByTheCurrentUser(EventDtoRequestUpdateStateAction eventDtoRequestUpdateStateAction,
                                                          long userId, long eventId);

    EventDtoResponse getObtainingDetailedInformationAboutAPublishedEventByItsId(long id, HttpServletRequest request);

    List<EventDtoResponse> searchForAnEvent(AdminEventFilterRequest adminEventFilterRequest);

    List<EventShortDto> getEvents(PublicEventFilterRequest publicEventFilterRequest, HttpServletRequest request);

    EventDtoResponse updatingEventDataAndStatus(EventDtoRequestUpdateStateAction eventDtoRequestUpdateStateAction,long eventId);

    List<RequestDto> getObtainingInformationAboutRequestsToParticipateInAnEventOfTheCurrentUser(long userId,
                                                                                                long eventId);

    EventRequestStatusUpdateResult updateChangingTheStatusOfApplicationsForParticipationInAnEventForTheCurrentUser(EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest,
                                                                                                                   long userId,
                                                                                                                   long eventId);
}