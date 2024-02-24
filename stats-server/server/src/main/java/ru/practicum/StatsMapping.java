package ru.practicum;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StatsMapping {
    public static Stats toStats(RequestStatsDto requestStatsDto) {
        return Stats.builder()
                .app(requestStatsDto.getApp())
                .uri(requestStatsDto.getUri())
                .ip(requestStatsDto.getIp())
                .timestamp(requestStatsDto.getTimestamp())
                .build();
    }

    public static ResponseStatsDto toResponseStatsDto(Stats stats) {
        return ResponseStatsDto.builder()
                .app(stats.getApp())
                .uri(stats.getUri())
                .hits(stats.getHits())
                .build();
    }

    public static RequestStatsDto toRequestStatsDto(Stats stats) {
        return RequestStatsDto.builder()
                .app(stats.getApp())
                .uri(stats.getUri())
                .ip(stats.getIp())
                .timestamp(stats.getTimestamp())
                .build();
    }
}
