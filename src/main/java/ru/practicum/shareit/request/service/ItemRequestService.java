package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto addItemRequest(long userId, ItemRequestDto itemRequestDto);

    List<ItemRequestResponseDto> getItemsRequests(long userId);

    List<ItemRequestResponseDto> getAllRequests(long userId, int from, int size);

    ItemRequestResponseDto getRequestById(Long userId, long requestId);

}
