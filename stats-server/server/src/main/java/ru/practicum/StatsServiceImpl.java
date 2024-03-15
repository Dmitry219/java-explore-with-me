package ru.practicum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.ValidationUserException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    public StatsServiceImpl(StatsRepository statsRepository) {
        this.statsRepository = statsRepository;
    }

    @Transactional
    @Override
    public ResponseStatsDto createStats(RequestStatsDto requestStatsDto) {
        Stats stats = statsRepository.save(StatsMapping.toStats(requestStatsDto));
        return StatsMapping.toResponseStatsDto(stats);
    }

    @Override
    public List<ResponseStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        log.info("Проверка сервис метода getRequestInformation " +
                "start {}" +
                "end {}" +
                "uris {}" +
                "unique {}",start, end, uris, unique);
        checkTimeEndAndStart(start, end);
        List<Stats> statsList = new ArrayList<>();
        if (unique && uris != null) { // true уникалыне ip
            statsList = statsRepository.getByDistinctIpAndTimestampAfterAndTimestampBeforeAndUriIn(start,end,uris);
            log.info("Проверка сервис метод getStats true statsList {}", statsList);
        } else if (!unique && uris != null) { //false все ip
            statsList = statsRepository.getByTimestampAfterAndTimestampBeforeAndUriIn(start,end,uris);
            log.info("Проверка сервис метод getStats false statsList {}", statsList);
        } else if (unique && uris == null) {
            statsList = statsRepository.getByDistinctIpAndTimestampAfterAndTimestampBefore(start,end);
        } else if (!unique && uris == null) {
            statsList = statsRepository.getByTimestampAfterAndTimestampBefore(start,end);
        }
        return statsList.stream()
                .map(StatsMapping::toResponseStatsDto)
                .collect(Collectors.toList());
    }

    private void checkTimeEndAndStart(LocalDateTime start, LocalDateTime end) {
        log.info("Проверка сервис метод checkTimeEnd проверка TIME");

        if (end.isBefore(start)) {
            throw new ValidationUserException("Вермя End предшествует Start!");
        } else if (end.equals(start)) {
            throw new ValidationUserException("Вермя End равно Start!");
        }
    }
}
