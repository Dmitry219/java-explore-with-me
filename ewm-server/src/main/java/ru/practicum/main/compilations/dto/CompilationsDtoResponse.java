package ru.practicum.main.compilations.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.main.evente.dto.EventShortDto;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompilationsDtoResponse {
    private List<EventShortDto> events = new ArrayList<>();
    private Long id;
    private boolean pinned;
    private String title;
}
