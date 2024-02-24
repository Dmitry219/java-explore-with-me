package ru.practicum;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;


@RestController
@Slf4j
public class StatsController {
    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @PostMapping(path = "/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void creatRequestInformation(@Valid @RequestBody RequestStatsDto requestStatsDto) {
        log.info("Проверка контроллера метода creatRequestInformation requestStatsDto {}",requestStatsDto);
        statsService.createStats(requestStatsDto);
    }

    @GetMapping(path = "/stats")
    public List<ResponseStatsDto> getRequestInformation(@RequestParam(value = "start") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                      @RequestParam(value = "end") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                      @RequestParam(value = "uris", required = false) List<String> uris,
                                      @RequestParam(value = "unique", defaultValue = "false") @NonNull Boolean unique) {
        log.info("Проверка контроллера метода getRequestInformation " +
                "start {}" +
                "end {}" +
                "uris {}" +
                "unique {}",start, end, uris, unique);
        return statsService.getStats(start, end, uris, unique);
    }
}
