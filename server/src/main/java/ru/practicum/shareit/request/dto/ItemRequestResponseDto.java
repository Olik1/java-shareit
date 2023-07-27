package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ItemRequestResponseDto {
    private final String description;
    private Long id;
    private List<ItemRequestResponseDtoItem> items;
    private LocalDateTime created;

}
