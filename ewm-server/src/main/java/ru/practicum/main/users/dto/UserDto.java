package ru.practicum.main.users.dto;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
public class UserDto {
    @Email(message = "Элекртонная почта не соответствует формату.")
    @NotBlank
    @Length(min = 6, max = 254)
    private String email;
    private Long id;
    @NotBlank
    @Length(min = 2, max = 250)
    private String name;
}
