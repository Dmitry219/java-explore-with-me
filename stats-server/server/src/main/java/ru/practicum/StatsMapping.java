package ru.practicum;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StatsMapping {
    public Stats toStats(RequestStatsDto requestStatsDto) {
        return Stats.builder()
                .app(requestStatsDto.getApp())
                .uri(requestStatsDto.getUri())
                .ip(requestStatsDto.getIp())
                .timestamp(requestStatsDto.getTimestamp())
                .build();
    }

    public ResponseStatsDto toResponseStatsDto(Stats stats) {
        return ResponseStatsDto.builder()
                .app(stats.getApp())
                .uri(stats.getUri())
                .hits(stats.getHits())
                .build();
    }

    public RequestStatsDto toRequestStatsDto(Stats stats) {
        return RequestStatsDto.builder()
                .app(stats.getApp())
                .uri(stats.getUri())
                .ip(stats.getIp())
                .timestamp(stats.getTimestamp())
                .build();
    }
}
