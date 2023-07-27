package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemItemRequestDto {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    //    private User owner;
//    private ItemRequest request;
//    private BookingDto lastBooking;
//    private BookingDto nextBooking;
    private long requestId;
//    private List<CommentDto> comments = new ArrayList<>();


}
