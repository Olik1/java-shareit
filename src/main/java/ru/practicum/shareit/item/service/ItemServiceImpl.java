package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemDao itemDao;
    private final UserService userService;

    @Override
    public ItemDto addItem(long userId, ItemDto itemDto) {
        validateItemDto(itemDto, false);
        Item item = ItemMapper.toItem(itemDto);
        UserDto user = userService.getUserById(userId);
        item.setOwner(UserMapper.toUser(user));
        log.info("Добавлна новая вещь; {}", itemDto.getName());
        return ItemMapper.toItemDto(itemDao.addItem(item));
    }

    @Override
    public ItemDto updateItem(long userId, ItemDto itemDto) {
        Item item = ItemMapper.toItem(itemDto);
        Item currentItem = itemDao.getItemByUserId(itemDto.getId());
        if (currentItem == null) {
            throw new ObjectNotFoundException("Такой вещи не существует!");
        }
        if (userId != currentItem.getOwner().getId()) {
            throw new ObjectNotFoundException("id вещи пользователя не совпадают с id владелььца вещи");
        }

        item.setOwner(currentItem.getOwner());
        if (itemDto.getName() == null) {
            item.setName(currentItem.getName());
        }
        if (itemDto.getDescription() == null) {

            item.setDescription(currentItem.getDescription());
        }

        if (itemDto.getAvailable() == null) {

            item.setAvailable(currentItem.isAvailable());
        }

        validateItemDto(itemDto, true);
        log.info("Обновлены данные по вещи; {}", item.getName());

        return ItemMapper.toItemDto(itemDao.updateUser(item));
    }

    @Override
    public ItemDto getItemByUserId(long id) {
        return ItemMapper.toItemDto(itemDao.getItemByUserId(id));
    }

    @Override
    public List<ItemDto> getItemsByUserId(long userId) {
        List<ItemDto> itemDtos = new ArrayList<>();
        List<Item> items = itemDao.getItemsByUserId(userId);
        for (Item item : items) {
            itemDtos.add(ItemMapper.toItemDto(item));
        }
        return itemDtos;
    }

    @Override
    public List<ItemDto> searchText(String text) {
        List<ItemDto> itemDtos = new ArrayList<>();
        if (text.isBlank()) {
            return itemDtos;
        }
        List<Item> items = itemDao.searchText(text);
        for (Item item : items) {
            itemDtos.add(ItemMapper.toItemDto(item));
        }
        return itemDtos;
    }

    private void validateItemDto(ItemDto itemDto, boolean isUpdate) {
        if (isUpdate && (itemDto.getName() != null && itemDto.getName().isBlank()) ||
                (!isUpdate && (itemDto.getName() == null || itemDto.getName().isBlank()))) {
            throw new ValidationException("Не указано поле Name");
        }

        if (isUpdate && (itemDto.getDescription() != null && itemDto.getDescription().isBlank()) ||
                !isUpdate && (itemDto.getDescription() == null || itemDto.getDescription().isBlank())) {
            throw new ValidationException("Не указано поле Description");
        }

        if (!isUpdate && itemDto.getAvailable() == null) {
            throw new ValidationException("Отсуствует поле Available");
        }

    }
}
