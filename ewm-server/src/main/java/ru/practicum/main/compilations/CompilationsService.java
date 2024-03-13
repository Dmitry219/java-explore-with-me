package ru.practicum.main.compilations;

import ru.practicum.main.compilations.dto.CompilationsDtoRequest;
import ru.practicum.main.compilations.dto.CompilationsDtoResponse;
import ru.practicum.main.compilations.dto.UpdateCompilationRequest;

import java.util.List;

public interface CompilationsService {
    CompilationsDtoResponse createCompilations(CompilationsDtoRequest compilationsDtoRequest);

    void deleteCompilations(long compId);

    CompilationsDtoResponse updateSelectionInformation(UpdateCompilationRequest updateCompilationRequest,
                                                       long compId);

    List<CompilationsDtoResponse> getCompilations(Boolean pinned, int from, int size);

    CompilationsDtoResponse getCompilationsById(long compId);
}
