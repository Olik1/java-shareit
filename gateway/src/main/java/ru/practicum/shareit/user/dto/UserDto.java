package ru.practicum.shareit.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import static ru.practicum.shareit.validation.ValidationGroups.Create;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    @JsonProperty("id")
    private long id;
    @JsonProperty("name")
    @NotBlank(groups = Create.class, message = "Имя не может быть пустым")
    private String name;
    @JsonProperty("email")
    @NotBlank(groups = Create.class, message = "E-mail не может быть пустым")
    @Email(message = "Введен некорректный e-mail")
    private String email;

}
