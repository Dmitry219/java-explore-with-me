package ru.practicum.main.compilations.dto;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class UpdateCompilationRequest {
    private List<Long> events = new ArrayList<>();
    private Boolean pinned;
    @Length(min = 1, max = 50)
    private String title;
}
