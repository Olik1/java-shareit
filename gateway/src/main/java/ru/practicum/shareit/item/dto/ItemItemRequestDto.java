package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.validation.ValidationGroups;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class ItemItemRequestDto {
    private long id;
    @NotBlank(groups = ValidationGroups.Create.class, message = "Название не может быть пустым")
    private String name;
    @NotBlank(groups = ValidationGroups.Create.class, message = "Описание не может быть пустым")
    @Size(max = 200, message = "Длина описания должна до 200 символов")
    private String description;
    @NotNull(groups = ValidationGroups.Create.class, message = "Поле доступности вещи не может быть пустым")
    private Boolean available;
    private long requestId;
}