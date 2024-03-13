package ru.practicum.main.request;

import ru.practicum.main.request.dto.RequestDto;

import java.util.List;

public interface RequestService {
    RequestDto createRrequest(long userId, long eventId);

    RequestDto updateRrequest(long userId, long eventId);

    List<RequestDto> getObtainingInformationAboutTheCurrentUserRequestsToParticipateInOtherPeoplesEvents(long userId);
}
