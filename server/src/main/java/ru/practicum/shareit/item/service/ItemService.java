package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemItemRequestDto;

import java.util.List;

public interface ItemService {

    ItemDto addItem(long userId, ItemItemRequestDto itemDto);

    ItemDto updateItem(long userId, ItemItemRequestDto itemDto);

    ItemDto getItem(long itemId, long userId);

    List<ItemDto> getItemsByUserId(long userId, int from, int size);

    List<ItemDto> searchText(String text, int from, int size);

    CommentDto addComment(long userId, long itemId, CommentDto commentDto);

}
