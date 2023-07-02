package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemsDto;

import java.util.List;

public interface ItemService {

    ItemDto addItem(long userId, ItemDto itemDto);

    ItemDto updateItem(long userId, ItemDto itemDto);

    ItemDto getItem(long itemId, long userId);

    List<ItemsDto> getItemsByUserId(long userId);

    List<ItemDto> searchText(String text);
    CommentDto addComment(long userId, long itemId, CommentDto commentDto);

}
