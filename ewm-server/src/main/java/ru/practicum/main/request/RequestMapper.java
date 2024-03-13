package ru.practicum.main.request;

import lombok.experimental.UtilityClass;
import ru.practicum.main.evente.StatusType;
import ru.practicum.main.request.dto.RequestDto;

@UtilityClass
public class RequestMapper {
    public RequestDto toRequestDto(Request request) {
        return RequestDto.builder()
                .created(request.getCreated())
                .event(request.getEvent().getId())
                .id(request.getId())
                .requester(request.getRequester().getId())
                .status(StatusType.valueOf(request.getStatus()))
                .build();
    }

}
