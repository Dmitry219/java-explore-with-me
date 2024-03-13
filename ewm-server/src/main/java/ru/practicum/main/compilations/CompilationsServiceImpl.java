package ru.practicum.main.compilations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.compilations.dto.CompilationsDtoRequest;
import ru.practicum.main.compilations.dto.CompilationsDtoResponse;
import ru.practicum.main.compilations.dto.UpdateCompilationRequest;
import ru.practicum.main.evente.EventMapping;
import ru.practicum.main.evente.EventRepository;
import ru.practicum.main.exception.NotFoundExceptionConflict;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
@Slf4j
public class CompilationsServiceImpl implements CompilationsService {
    private final CompilationsRepository compilationsRepository;
    private final EventRepository eventRepository;

    public CompilationsServiceImpl(CompilationsRepository compilationsRepository, EventRepository eventRepository) {
        this.compilationsRepository = compilationsRepository;
        this.eventRepository = eventRepository;
    }

    @Transactional
    @Override
    public CompilationsDtoResponse createCompilations(CompilationsDtoRequest compilationsDtoRequest) {
        log.info("Сервис createCompilations проверка compilationsDtoRequest = {}", compilationsDtoRequest);

        Compilations compilations = new Compilations();
        if (compilationsDtoRequest.getPinned() == null) {
            compilations.setPinned(false);
        } else {
            compilations.setPinned(compilationsDtoRequest.getPinned());
        }

        if (compilationsDtoRequest.getEvents() == null) {
            compilations.setEvents(new ArrayList<>());
        } else {
            compilations.setEvents(eventRepository.findAllByIdIn(compilationsDtoRequest.getEvents()));
        }

        compilations.setTitle(compilationsDtoRequest.getTitle());

        log.info("Сервис createCompilations проверка compilations = {}", compilations);
        compilations = compilationsRepository.save(compilations);
        log.info("Сервис createCompilations проверка после сохранения compilations = {}", compilations);

        CompilationsDtoResponse compilationsDtoResponse = new CompilationsDtoResponse();
        if (!compilations.getEvents().isEmpty()) {
            compilationsDtoResponse.setEvents(compilations.getEvents().stream()
                    .map(EventMapping::toEventShortDto)
                    .collect(Collectors.toList()));
        }

        compilationsDtoResponse.setId(compilations.getId());

        compilationsDtoResponse.setTitle(compilations.getTitle());
        compilationsDtoResponse.setPinned(compilations.isPinned());
        return compilationsDtoResponse;
    }

    @Transactional
    @Override
    public void deleteCompilations(long compId) {
        log.info("Сервис deleteCompilations проверка compId = {}", compId);
        checkIdCompilations(compId);
        compilationsRepository.deleteById(compId);
    }

    @Transactional
    @Override
    public CompilationsDtoResponse updateSelectionInformation(UpdateCompilationRequest updateCompilationRequest,
                                                              long compId) {
        log.info("Сервис updateSelectionInformation проверка compilationsDtoRequest = {}", updateCompilationRequest);
        log.info("Сервис updateSelectionInformation проверка compId = {}", compId);

        Compilations compilationsUpdate = compilationsRepository.findById(compId).get();

        if (updateCompilationRequest.getPinned() != null) {
            compilationsUpdate.setPinned(updateCompilationRequest.getPinned());
        }
        if (updateCompilationRequest.getEvents() == null) {
            compilationsUpdate.setEvents(new ArrayList<>());
        } else {
            if (!updateCompilationRequest.getEvents().isEmpty()) {
                updateCompilationRequest.getEvents().stream().peek(this::checkEvent);
                compilationsUpdate.setEvents(eventRepository.findAllByIdIn(updateCompilationRequest.getEvents()));
            } else {
                compilationsUpdate.setEvents(new ArrayList<>());
            }
        }


        if (updateCompilationRequest.getTitle() != null) {
            compilationsUpdate.setTitle(compilationsUpdate.getTitle());
        }

        Compilations compilationsSave = compilationsRepository.save(compilationsUpdate);
        CompilationsDtoResponse compilationsDtoResponse = new CompilationsDtoResponse();
        compilationsDtoResponse.setId(compilationsSave.getId());
        if (!compilationsSave.getEvents().isEmpty()) {
            compilationsDtoResponse.setEvents(compilationsSave.getEvents().stream()
                    .map(EventMapping::toEventShortDto)
                    .collect(Collectors.toList()));
        }
        compilationsDtoResponse.setPinned(compilationsSave.isPinned());
        compilationsDtoResponse.setTitle(compilationsSave.getTitle());


        return compilationsDtoResponse;
    }

    @Override
    public List<CompilationsDtoResponse> getCompilations(Boolean pinned, int from, int size) {
        log.info("Сервис getCompilations проверка pinned = {}", pinned);
        log.info("Сервис getCompilations проверка from = {}", from);
        log.info("Сервис getCompilations проверка size = {}", size);

        Pageable pageable = PageRequest.of(from / size,size);

        List<Compilations> compilations = new ArrayList<>();
        if (pinned != null) {
            compilations = compilationsRepository.findAllByPinnedIs(pinned, pageable);
        } else {
            compilations = compilationsRepository.findAll(pageable).getContent();
        }


        log.info("Сервис getCompilations проверка compilations = {}", compilations);

        return compilations.stream()
                .map(CompilationsMapper::toCompilationsDtoResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationsDtoResponse getCompilationsById(long compId) {
        log.info("Сервис getCompilationsById проверка compId = {}", compId);
        checkIdCompilations(compId);
        Compilations compilation = compilationsRepository.findById(compId).get();
        CompilationsDtoResponse compilationsDtoResponse = new CompilationsDtoResponse();
        log.info("Сервис getCompilationsById проверка compilation = {}", compilation);
        if (!compilation.getEvents().isEmpty()) {
            compilationsDtoResponse.setEvents(compilation.getEvents().stream()
                    .map(EventMapping::toEventShortDto)
                    .collect(Collectors.toList()));
        }
        compilationsDtoResponse.setId(compilation.getId());
        compilationsDtoResponse.setPinned(compilation.isPinned());
        compilationsDtoResponse.setTitle(compilation.getTitle());
        return compilationsDtoResponse;
    }

    private void checkIdCompilations(long compId) {
        log.info("Проверка сервис метод checkIdCompilations проверка compId");
        if (!compilationsRepository.existsById(compId)) {
            throw new NotFoundExceptionConflict(String.format("Compilations с таким id = %s  не существует!", compId));
        }
    }

    private void checkEvent(long eventId) {
        log.info("Проверка сервис метод checkOwnerEvent проверка ID Event в");
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundExceptionConflict(String.format("Event с таким id = %s  не существует!", eventId));
        }
    }
}
