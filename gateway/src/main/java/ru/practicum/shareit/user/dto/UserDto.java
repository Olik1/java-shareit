package ru.practicum.shareit.user.dto;
import static ru.practicum.shareit.validation.ValidationGroups.Create;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
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
