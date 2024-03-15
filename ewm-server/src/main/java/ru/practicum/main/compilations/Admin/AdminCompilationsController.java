package ru.practicum.main.compilations.Admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.compilations.CompilationsService;
import ru.practicum.main.compilations.dto.CompilationsDtoRequest;
import ru.practicum.main.compilations.dto.CompilationsDtoResponse;
import ru.practicum.main.compilations.dto.UpdateCompilationRequest;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin/compilations")
@Slf4j
public class AdminCompilationsController {
    private final CompilationsService compilationsService;

    public AdminCompilationsController(CompilationsService compilationsService) {
        this.compilationsService = compilationsService;
    }

    //Добавление новой подборки(подборка может не содержать событие)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationsDtoResponse createCompilations(@Valid @RequestBody CompilationsDtoRequest compilationsDtoRequest) {
        log.info("Контроллер метод createCompilations проверка compilationsDtoRequest = {}", compilationsDtoRequest);
        return compilationsService.createCompilations(compilationsDtoRequest);
    }

    //Удаление подборки
    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilations(@PathVariable long compId) {
        log.info("Контроллер метод deleteCompilations проверка compId = {}", compId);
        compilationsService.deleteCompilations(compId);
    }

    //Обновить информацию о подборке
    @PatchMapping("/{compId}")
    public CompilationsDtoResponse updateSelectionInformation(@Valid @RequestBody UpdateCompilationRequest updateCompilationRequest,
                                                       @PathVariable long compId) {
        log.info("Проверка контроллер метод updateSelectionInformation compilationsDtoRequest {}", updateCompilationRequest);
        log.info("Проверка контроллер метод updateSelectionInformation compId {}", compId);

        return compilationsService.updateSelectionInformation(updateCompilationRequest, compId);
    }

}
