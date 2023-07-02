package ru.practicum.shareit.item.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;

@UtilityClass
public class ItemMapper {
    public static ItemDto toItemDto(Item item) { // Item -> в объект ItemDto
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.isAvailable())
                .build();
    }

    public static Item toItem(ItemDto itemDto) { //  ItemDto -> в объект Item для сохр. в бд
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable() == null ? false : itemDto.getAvailable())
                .build();

    }
    public static ItemsDto toItemsDto(Item item, LocalDateTime last, LocalDateTime next) { // Items -> в объект ItemDto
        return ItemsDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.isAvailable())
                .last(last)
                .next(next)
                .build();
    }

    public static ItemsDto toItemsDto2(Item item) { // Items -> в объект ItemDto
        return ItemsDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.isAvailable())
                .build();
    }
}
