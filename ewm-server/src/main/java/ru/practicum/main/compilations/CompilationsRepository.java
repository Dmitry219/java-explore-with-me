package ru.practicum.main.compilations;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompilationsRepository extends JpaRepository<Compilations, Long> {

    List<Compilations> findAllByPinnedIs(Boolean pinned, Pageable pageRequest);
}
