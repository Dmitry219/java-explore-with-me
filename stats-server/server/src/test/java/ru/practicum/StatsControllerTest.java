package ru.practicum;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.exception.NotFoundException;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = StatsController.class)
public class StatsControllerTest {
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private StatsService statsService;
    @Autowired
    private MockMvc mvc;


    private RequestStatsDto requestStatsDto;
    private ResponseStatsDto responseStatsDtol;
    private Stats statsReq;
    private Stats statsResp;

    @BeforeEach
    void setUp() {
        statsReq = Stats.builder()
                .app("ewm-main-service")
                .uri(List.of("/events").toString())
                .ip("192.163.0.1")
                .timestamp(LocalDateTime.parse("2022-09-06T11:00:23"))
                .build();

        statsResp = Stats.builder()
                .app("ewm-main-service")
                .uri(List.of(List.of("/events").toString()).toString())
                .ip("192.163.0.1")
                .hits(1L)
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
                .hits(1L)
                .build();

    }

    @Test
    @SneakyThrows
    void createStatsStatusOk() {
        when(statsService.createStats(requestStatsDto)).thenReturn(responseStatsDtol);

        mvc.perform(post("/hit")
                        .content(mapper.writeValueAsString(requestStatsDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        verify(statsService, times(1)).createStats(any());
    }

    @Test
    @SneakyThrows
    void createStatsNotAppAndUriIsStatusBadRequest() {

        requestStatsDto = RequestStatsDto.builder()
                .app("")
                .uri(List.of().toString())
                .ip("192.163.0.1")
                .timestamp(LocalDateTime.parse("2022-09-06T11:00:23"))
                .build();

        mvc.perform(post("/hit")
                        .content(mapper.writeValueAsString(requestStatsDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(statsService, never()).createStats(any());
    }

    @Test
    @SneakyThrows
    void getRequestInformationStatusOk() {
        when(statsService.getStats(any(),any(),any(),any())).thenReturn(List.of(responseStatsDtol));

        mvc.perform(get("/stats")
                        .param("start","2022-09-06 11:00:22")
                        .param("end","2022-09-06 11:00:24")
                        .param("uris",List.of("/events").toString())
                        .param("unique","true"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(responseStatsDtol))))
                .andExpect(jsonPath("$", hasSize(1)));
        verify(statsService, times(1)).getStats(any(),any(),any(),any());
    }
}
