package ru.practicum;


import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    ResponseStatsDto createStats(RequestStatsDto requestStatsDto);

    List<ResponseStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
