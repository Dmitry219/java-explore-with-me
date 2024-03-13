package ru.practicum.main.compilations.Public;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.compilations.CompilationsService;
import ru.practicum.main.compilations.dto.CompilationsDtoResponse;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/compilations")
@Slf4j
public class PublicCompilationsController {
    private final CompilationsService compilationsService;

    public PublicCompilationsController(CompilationsService compilationsService) {
        this.compilationsService = compilationsService;
    }

    //Получение подборок событий
    @GetMapping
    public List<CompilationsDtoResponse> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                         @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero int from,
                                                         @RequestParam(value = "size", defaultValue = "10") @Positive int size) {
        log.info("Public Контроллер метод getCompilations проверка pinned = {}", pinned);
        log.info("Public Контроллер метод getCompilations проверка from = {}", from);
        log.info("Public Контроллер метод getCompilations проверка size = {}", size);

        return compilationsService.getCompilations(pinned, from, size);
    }

    //Получение подборки событий по его id
    @GetMapping(path = "/{compId}")
    public CompilationsDtoResponse getCompilationsById(@PathVariable long compId) {
        log.info("Public Контроллер метод getCompilationsById проверка compId = {}", compId);

        return compilationsService.getCompilationsById(compId);
    }

}
