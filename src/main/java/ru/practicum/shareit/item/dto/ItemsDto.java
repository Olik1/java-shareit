package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemsDto {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private User owner;
    private ItemRequest request;
    private LocalDateTime last;
    private LocalDateTime next;
    private BookingDto lastBooking;
    private BookingDto nextBooking;
    private List<CommentDto> comments = new ArrayList<>();

}
