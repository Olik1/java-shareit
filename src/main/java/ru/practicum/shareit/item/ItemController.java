package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@Slf4j
@AllArgsConstructor
public class ItemController {
    ItemService itemService;
    UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto addItem(@Valid @RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Добавлена вещь: {}", itemDto);
        return itemService.addItem(userId, itemDto);
    }
    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") long userId,
                              @RequestBody ItemDto itemDto) {

        log.info("Обновление вещи id: {}",  userId);
        return itemService.updateItem(userId, itemDto);
    }
    @GetMapping("/{id}")
    public ItemDto getItemById(@PathVariable long id) {
        return itemService.getItemByUserId(id);
    }
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> getItems(){
        return itemService.getAllItems();
    }
}
