package ru.practicum.shareit.request.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.request.model.ItemRequest;

@UtilityClass
public class ItemRequestMapper {
    public static ItemRequestDto toItemRequesDto(ItemRequest itemRequest) { // ItemRequest -> в объект ItemRequestDto
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .requestor(itemRequest.getRequester())
                .created(itemRequest.getCreated())
                .build();
    }

    public static ItemRequest toItemRequest(ItemRequestDto itemRequestDto) { //  ItemRequestDto -> в объект ItemRequest для сохр. в бд
        return ItemRequest.builder()
                .id(itemRequestDto.getId())
                .description(itemRequestDto.getDescription())
                .requester(itemRequestDto.getRequestor())
                .created(itemRequestDto.getCreated())
                .build();
    }
    public static ItemRequestResponseDto toItemRequestResponseDto(ItemRequest itemRequest) { // ItemRequest -> в объект ItemRequestDto
        return ItemRequestResponseDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .build();
    }

}
