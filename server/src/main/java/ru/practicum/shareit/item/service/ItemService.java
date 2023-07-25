package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto addItem(long userId, ItemDto itemDto);

    ItemDto updateItem(long userId, ItemDto itemDto);

    ItemDto getItem(long itemId, long userId);

    List<ItemDto> getItemsByUserId(long userId, int from, int size);

    List<ItemDto> searchText(String text, int from, int size);

    CommentDto addComment(long userId, long itemId, CommentDto commentDto);

}
