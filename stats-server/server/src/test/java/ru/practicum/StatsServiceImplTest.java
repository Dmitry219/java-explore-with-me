package ru.practicum;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = {"db.name=test"})
public class StatsServiceImplTest {
    @Mock
    private StatsRepository statsRepository;
    @InjectMocks
    private StatsServiceImpl statsService;

    private Stats statsReq;
    private Stats statsReq1;
    private Stats statsReq2;
    private Stats statsResp;
    private Stats statsResp1;
    private Stats statsResp2;
    private RequestStatsDto requestStatsDto;
    private ResponseStatsDto responseStatsDtol;
    private ResponseStatsDto responseStatsDtol1;
    private ResponseStatsDto responseStatsDtol2;

    @BeforeEach
    public void setUp() {
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
                .uri(List.of("/events").toString())
                .hits(2L)
                .build();

        statsResp2 = Stats.builder()
                .app("ewm-main-service")
                .uri(List.of("/events").toString())
                .hits(3L)
                .build();

        statsResp1 = Stats.builder()
                .app("ewm-main-service")
                .uri(List.of("/events").toString())
                .build();

        requestStatsDto = RequestStatsDto.builder()
                .app("ewm-main-service")
                .uri(List.of("/events").toString())
                .ip("192.163.0.1")
                .timestamp(LocalDateTime.parse("2022-09-06T11:00:23"))
                .build();

        responseStatsDtol = ResponseStatsDto.builder()
                .app("ewm-main-service")
                .uri(List.of("/events").toString())
                .build();

        responseStatsDtol1 = ResponseStatsDto.builder()
                .app("ewm-main-service")
                .uri(List.of("/events").toString())
                .hits(2L)
                .build();

        responseStatsDtol2 = ResponseStatsDto.builder()
                .app("ewm-main-service")
                .uri(List.of("/events").toString())
                .hits(3L)
                .build();
    }

    @Test
    void createStatsTrue() {
        when(statsRepository.save(any(Stats.class))).thenReturn(statsResp1);

        ResponseStatsDto statsDto = statsService.createStats(requestStatsDto);

        assertEquals(statsDto, responseStatsDtol);
    }

    @Test
    void getByDistinctIpAndTimestampAfterAndTimestampBeforeAndUriInTrue() {
        when(statsRepository.getByDistinctIpAndTimestampAfterAndTimestampBeforeAndUriIn(any(),any(),any()))
                .thenReturn(List.of(statsResp));

        List<ResponseStatsDto> users = statsService.getStats(statsReq.getTimestamp().minusMinutes(2),statsReq.getTimestamp().plusMinutes(2), List.of("/events"),true);

        Assertions.assertEquals(users, List.of(responseStatsDtol1));
    }

    @Test
    void getByTimestampAfterAndTimestampBeforeAndUriInTrue() {
        when(statsRepository.getByTimestampAfterAndTimestampBeforeAndUriIn(any(),any(),any()))
                .thenReturn(List.of(statsResp2));

        List<ResponseStatsDto> users = statsService.getStats(statsReq.getTimestamp().minusMinutes(2),statsReq.getTimestamp().plusMinutes(2), List.of("/events"),false);

        Assertions.assertEquals(users, List.of(responseStatsDtol2));
    }

    @Test
    void getByDistinctIpAndTimestampAfterAndTimestampBeforeTrue() {
        when(statsRepository.getByDistinctIpAndTimestampAfterAndTimestampBefore(any(),any()))
                .thenReturn(List.of(statsResp));

        List<ResponseStatsDto> users = statsService.getStats(statsReq.getTimestamp().minusMinutes(2),statsReq.getTimestamp().plusMinutes(2), null,true);

        Assertions.assertEquals(users, List.of(responseStatsDtol1));
    }

    @Test
    void getByTimestampAfterAndTimestampBeforeTrue() {
        when(statsRepository.getByTimestampAfterAndTimestampBefore(any(),any()))
                .thenReturn(List.of(statsResp2));

        List<ResponseStatsDto> users = statsService.getStats(statsReq.getTimestamp().minusMinutes(2),statsReq.getTimestamp().plusMinutes(2), null,false);

        Assertions.assertEquals(users, List.of(responseStatsDtol2));
    }
}
