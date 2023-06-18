package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemDao itemDao;
    private final UserService userService;

    @Override
    public ItemDto addItem(long userId, ItemDto itemDto) {
        Item item = ItemMapper.toItem(itemDto);
        UserDto user = userService.getUserById(userId);
        item.setOwner(UserMapper.toUser(user));
        log.info("Добавлна новая вещь; {}", itemDto.getName());
        return ItemMapper.toItemDto(itemDao.addItem(item));
    }

    @Override
    public ItemDto updateItem(long userId, ItemDto itemDto) {
        Item item = ItemMapper.toItem(itemDto);
        UserDto user = userService.getUserById(userId);
        item.setOwner(UserMapper.toUser(user));
        log.info("Обновлены данные по вещи; {}", itemDto.getName());

        return ItemMapper.toItemDto(itemDao.updateUser(item));
    }

    @Override
    public ItemDto getItemByUserId(long id) {

        return null;
    }

    @Override
    public List<ItemDto> getAllItems() {
        return null;
    }

    @Override
    public List<ItemDto> searchText(String text) {
        return null;
    }
}
