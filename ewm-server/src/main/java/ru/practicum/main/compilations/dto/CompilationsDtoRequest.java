package ru.practicum.main.compilations.dto;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class CompilationsDtoRequest {
    private List<Long> events = new ArrayList<>();
    private Boolean pinned;
    @NotBlank
    @Length(min = 1, max = 50)
    private String title;
}
