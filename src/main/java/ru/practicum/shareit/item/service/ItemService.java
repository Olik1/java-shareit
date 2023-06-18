package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto addItem(long userId, ItemDto itemDto);

    ItemDto updateItem(long userId, ItemDto itemDto);

    ItemDto getItemByUserId(long id);

    List<ItemDto> getAllItems();

    List<ItemDto> searchText(String text);
}
