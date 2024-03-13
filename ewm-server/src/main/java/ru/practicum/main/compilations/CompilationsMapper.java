package ru.practicum.main.compilations;

import lombok.experimental.UtilityClass;
import ru.practicum.main.compilations.dto.CompilationsDtoRequest;
import ru.practicum.main.compilations.dto.CompilationsDtoResponse;
import ru.practicum.main.evente.EventMapping;

import java.util.stream.Collectors;

@UtilityClass
public class CompilationsMapper {
    public Compilations toCompilations(CompilationsDtoRequest compilationsDtoRequest) {
        return Compilations.builder()
                .title(compilationsDtoRequest.getTitle())
                .build();
    }

    public CompilationsDtoResponse toCompilationsDtoResponse(Compilations compilations) {
        return CompilationsDtoResponse.builder()
                .events(compilations.getEvents().stream()
                        .map(EventMapping::toEventShortDto)
                        .collect(Collectors.toList()))
                .id(compilations.getId())
                .pinned(compilations.isPinned())
                .title(compilations.getTitle())
                .build();
    }
}
