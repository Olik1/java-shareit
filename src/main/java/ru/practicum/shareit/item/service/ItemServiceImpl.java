package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.CommentRepository;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;
import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;

    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;


    @Override
    public ItemDto addItem(long userId, ItemDto itemDto) {
        validateItemDto(itemDto, false);
        Item item = ItemMapper.toItem(itemDto);
        UserDto user = userService.getUserById(userId);
        item.setOwner(UserMapper.toUser(user));
        log.info("Добавлна новая вещь; {}", itemDto.getName());
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto updateItem(long userId, ItemDto itemDto) {
        Item item = ItemMapper.toItem(itemDto);
        var changeItem1 = itemRepository.findAll();
        var changeItem = itemRepository.findById(itemDto.getId());
        var currentItem = changeItem.get();

        if (currentItem == null) {
            throw new ObjectNotFoundException("Такой вещи не существует!");
        }
        if (userId != currentItem.getOwner().getId()) {
            throw new ObjectNotFoundException("id вещи пользователя не совпадают с id владелььца вещи");
        }
        item.setOwner(currentItem.getOwner());

        if (itemDto.getName() != null) {
            currentItem.setName(item.getName());
        }
        if (itemDto.getDescription() != null) {
            currentItem.setDescription(item.getDescription());
        }

        if (itemDto.getAvailable() != null) {

            currentItem.setAvailable(item.isAvailable());
        }

        validateItemDto(itemDto, true);
        log.info("Обновлены данные по вещи; {}", currentItem.getName());

        return ItemMapper.toItemDto(itemRepository.save(currentItem));
    }

    @Override
    public ItemDto getItem(long itemId, long userId) {
        var changeItem1 = itemRepository.findAll();
        var item = itemRepository.findById(itemId).orElseThrow(() ->
                new ObjectNotFoundException("Вещь не найдена!"));
        List<Comment> comments = commentRepository.findCommentsByItem_Id(itemId);
        var itemDto = ItemMapper.toItemDto(item);
        List<CommentDto> commenstDto = new ArrayList<>();
        for (Comment comment : comments) {
            commenstDto.add(CommentMapper.tocommentDto(comment));
        }
        itemDto.setComments(commenstDto);

        if (userId == item.getOwner().getId()) {
            var bookings = bookingRepository.findBookingByItem_IdAndStatus(item.getId(), Status.APPROVED);

            if (bookings.size() != 0) {
                bookings = bookings.stream()
                        .sorted(Comparator.comparing(Booking::getStarts).reversed())
                        .collect(Collectors.toList());

                for (Booking booking : bookings) {
                    if (booking.getStarts().isBefore(LocalDateTime.now())) {
                        itemDto.setLastBooking(BookingMapper.toBookingDto(booking));
                        break;
                    }
                }

                bookings = bookings.stream()
                        .sorted(Comparator.comparing(Booking::getStarts))
                        .collect(Collectors.toList());

                for (Booking booking : bookings) {
                    if (booking.getStarts().isAfter(LocalDateTime.now())) {
                        itemDto.setNextBooking(BookingMapper.toBookingDto(booking));
                        break;
                    }
                }
            }
        }
        return itemDto;
    }

    @Override
    public List<ItemDto> getItemsByUserId(long userId) {
        List<ItemDto> itemDtos = new ArrayList<>();
        List<Item> items = itemRepository.findItemByOwnerId(userId);

        items = items.stream()
                .sorted(Comparator.comparingLong(Item::getId))
                .collect(Collectors.toList());

        for (Item item : items) {
            var itemDto = ItemMapper.toItemDto(item);

            if (userId == item.getOwner().getId()) {
                var bookings = bookingRepository.findBookingByItem_IdAndStatus(item.getId(), Status.APPROVED);

                if (bookings.size() > 0) {
                    bookings = bookings.stream()
                            .sorted(Comparator.comparing(Booking::getStarts).reversed())
                            .collect(Collectors.toList());

                    for (Booking booking : bookings) {
                        if (booking.getStarts().isBefore(LocalDateTime.now())

                        ) {
                            itemDto.setLastBooking(BookingMapper.toBookingDto(booking));
                            break;
                        }
                    }
                    bookings = bookings.stream()
                            .sorted(Comparator.comparing(Booking::getStarts))
                            .collect(Collectors.toList());
                    for (Booking booking : bookings) {
                        if (booking.getStarts().isAfter(LocalDateTime.now())) {
                            itemDto.setNextBooking(BookingMapper.toBookingDto(booking));
                            break;
                        }
                    }
                }
            }

            List<Comment> comments = commentRepository.findCommentsByItem_Id(item.getId());
            List<CommentDto> commenstDto = new ArrayList<>();
            for (Comment comment : comments) {
                commenstDto.add(CommentMapper.tocommentDto(comment));
            }
            itemDto.setComments(commenstDto);

            itemDtos.add(itemDto);
        }
        return itemDtos;
    }

    @Override
    public List<ItemDto> searchText(String text) {
        List<ItemDto> itemDtos = new ArrayList<>();
        if (text.isBlank()) {
            return itemDtos;
        }
        List<Item> items = itemRepository.search(text);
        for (Item item : items) {
            if (item.isAvailable()) {
                itemDtos.add(ItemMapper.toItemDto(item));
            }
        }

        return itemDtos;
    }

    @Override
    public CommentDto addComment(long userId, long itemId, CommentDto commentDto) {

        var need = itemRepository.findAll();
        var need1 = bookingRepository.findAll();

        var ItemOptional = itemRepository.findById(itemId);

        if (ItemOptional.isEmpty()) {
            throw new ObjectNotFoundException("Такой вещи нет");
        }
        var item = ItemOptional.get();

        var userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty()) {
            throw new ObjectNotFoundException("Такого пользователя нет");
        }
        var user = userOptional.get();

        List<Booking> bookings = bookingRepository.findBookingByItem_Id(itemId);
        boolean isExist = false;
        for (Booking booking : bookings) {
            if (booking.getBooker().getId() == userId
                    && booking.getStarts().isBefore(LocalDateTime.now())
                    && booking.getStatus().equals(Status.APPROVED)) {
                isExist = true;
                break;
            }
        }
        if (!isExist) {
            throw new ValidationException("Этой вещью не пользовался данный пользователь!");
        }

        Comment comment = CommentMapper.toComment(commentDto);
        comment.setAuthor(user);
        comment.setItem(item);
        comment.setCreated(LocalDateTime.now());
        var result = CommentMapper.tocommentDto(commentRepository.save(comment));
        return result;
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
