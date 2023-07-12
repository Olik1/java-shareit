package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ItemRequestResponseDtoItem {
    private long id;
    private String name;
    private String description;
    private long requestId;
    private boolean available;
}
