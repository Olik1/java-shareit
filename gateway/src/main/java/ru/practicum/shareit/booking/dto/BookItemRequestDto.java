package ru.practicum.shareit.booking.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;

import lombok.*;
@Setter
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookItemRequestDto {
	@NotNull(message = "Не указана вещь")
	private long itemId;
	@FutureOrPresent
	@NotNull(message = "Дата начала бронирования не может быть пустой")
	private LocalDateTime start;
	@Future
	@NotNull(message = "Дата окончания бронирования не может быть пустой")
	private LocalDateTime end;

}
