package ru.practicum.shareit.request.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto addItemRequest(long userId, ItemRequestDto itemRequestDto);

    List<ItemRequestResponseDto> getItemsRequests(long userId);
    List<ItemRequestDto> getAllRequests(long userId, int from, int size);

}
