package ru.practicum.shareit.item.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestResponseDtoItem;

@UtilityClass
public class ItemMapper {
    public static ItemDto toItemDto(Item item) { // Item -> в объект ItemDto
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.isAvailable())
                .requestId(item.getRequest() == null ? 0 : item.getRequest().getId())
                .build();
    }

//    public static Item toItem(ItemDto itemDto) { //  ItemDto -> в объект Item для сохр. в бд
//        return Item.builder()
//                .id(itemDto.getId())
//                .name(itemDto.getName())
//                .description(itemDto.getDescription())
//                .available(itemDto.getAvailable() == null ? false : itemDto.getAvailable())
//                .build();
//
//    }

    public static Item toItem(ItemItemRequestDto itemDto) { //  ItemItemRequestDto -> в объект Item для сохр. в бд
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable() == null ? false : itemDto.getAvailable())
                .build();

    }

    public static ItemRequestResponseDtoItem toItemRequestResponseDtoItem(Item item) { // Item -> в объект ItemDto
        return ItemRequestResponseDtoItem.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.isAvailable())
                .requestId(item.getRequest() == null ? 0 : item.getRequest().getId())
                .build();
    }

}
