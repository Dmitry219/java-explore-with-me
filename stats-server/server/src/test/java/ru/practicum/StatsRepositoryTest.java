package ru.practicum;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
public class StatsRepositoryTest {
    @Autowired
    StatsRepository statsRepository;

    private Stats statsReq;
    private Stats statsReq1;
    private Stats statsReq2;
    private Stats statsResp;
    private Stats statsResp1;

    @BeforeEach
    public void init() {

        statsReq = Stats.builder()
                .app("ewm-main-service")
                .uri("/events")
                .ip("192.163.0.1")
                .timestamp(LocalDateTime.parse("2022-09-06T11:00:23"))
                .build();

        statsReq1 = Stats.builder()
                .app("ewm-main-service")
                .uri("/events")
                .ip("192.163.0.2")
                .timestamp(LocalDateTime.parse("2022-09-06T11:00:24"))
                .build();

        statsReq2 = Stats.builder()
                .app("ewm-main-service")
                .uri("/events")
                .ip("192.163.0.2")
                .timestamp(LocalDateTime.parse("2022-09-06T11:00:25"))
                .build();

        statsRepository.save(statsReq);
        statsRepository.save(statsReq1);
        statsRepository.save(statsReq2);

        statsResp = Stats.builder()
                .app("ewm-main-service")
                .uri("/events")
                .hits(2L)
                .build();

        statsResp1 = Stats.builder()
                .app("ewm-main-service")
                .uri("/events")
                .hits(3L)
                .build();

    }

    @Test
    void getByDistinctIpAndTimestampAfterAndTimestampBeforeAndUriInTrue() {
        List<Stats> statsNew = statsRepository.getByDistinctIpAndTimestampAfterAndTimestampBeforeAndUriIn(statsReq.getTimestamp().minusMinutes(2),
                statsReq2.getTimestamp().plusMinutes(2), List.of("/events"));
        Assertions.assertEquals(statsNew, List.of(statsResp));
    }

    @Test
    void getByTimestampAfterAndTimestampBeforeAndUriInTrue() {
        List<Stats> statsNew = statsRepository.getByTimestampAfterAndTimestampBeforeAndUriIn(statsReq.getTimestamp().minusMinutes(2),
                statsReq.getTimestamp().plusMinutes(3), List.of("/events"));

        Assertions.assertEquals(statsNew, List.of(statsResp1));
    }

    @Test
    void getByDistinctIpAndTimestampAfterAndTimestampBeforeTrue() {
       List<Stats> statsNew = statsRepository.getByDistinctIpAndTimestampAfterAndTimestampBefore(statsReq.getTimestamp().minusMinutes(2),
               statsReq.getTimestamp().plusMinutes(3));

        Assertions.assertEquals(statsNew, List.of(statsResp));
    }

    @Test
    void getByTimestampAfterAndTimestampBeforeTrue() {
        List<Stats> statsNew = statsRepository.getByTimestampAfterAndTimestampBefore(statsReq.getTimestamp().minusMinutes(2),
                statsReq.getTimestamp().plusMinutes(3));

        Assertions.assertEquals(statsNew, List.of(statsResp1));
    }
}
