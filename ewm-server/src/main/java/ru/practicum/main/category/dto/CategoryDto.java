package ru.practicum.main.category.dto;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class CategoryDto {
    private Long  id;
    @NotBlank
    @Length(max = 50)
    private String name;
}
