package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public ItemRequestDto addItemRequest(long userId, ItemRequestDto itemRequestDto) {
        var userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty()) {
            throw new ObjectNotFoundException("Такого пользователя не существует!");
        }
        var user = userOptional.get();
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto);
        itemRequest.setRequester(user);
        itemRequest = itemRequestRepository.save(itemRequest);

        return ItemRequestMapper.toItemRequesDto(itemRequest);
    }

    @Override
    public List<ItemRequestResponseDto> getItemsRequests(long userId) {
        userRepository.findById(userId).orElseThrow(() -> new ObjectNotFoundException("Пользователь с id %d не найден"));
        var itemRequests = itemRequestRepository.findItemRequestsByRequesterId(userId);

        List<Long> requestIds = itemRequests.stream()
                .map(ItemRequest::getId)
                .collect(Collectors.toList());
        List<Item> itemList = itemRepository.findItemsByRequestIdIn(requestIds); // все вещи по запросу

        List<ItemRequestResponseDto> result = itemRequests.stream()
                .map(ItemRequestMapper::toItemRequestResponseDto)
                .collect(Collectors.toList());

        for (ItemRequestResponseDto itemRequestResponseDto : result) {
            if (itemRequestResponseDto.getItems() == null) {
                itemRequestResponseDto.setItems(new ArrayList<>());
            }
            for (Item item : itemList) {
                if (Objects.equals(item.getRequest().getId(), itemRequestResponseDto.getId())) {

                    itemRequestResponseDto.getItems().add(ItemMapper.toItemRequestResponseDtoItem(item));

                }
            }
        }

        return result;
    }

    @Override
    public List<ItemRequestResponseDto> getAllRequests(long userId, int from, int size) {
        int offset = from > 0 ? from / size : 0;
        PageRequest page = PageRequest.of(offset, size);

        Page<ItemRequest> itemRequestList = itemRequestRepository.findByOrderByCreatedDesc(page);

        List<Long> requestIds = itemRequestList.stream()
                .map(ItemRequest::getId)
                .collect(Collectors.toList());

        List<Item> itemList = itemRepository.findItemsByRequestIdIn(requestIds); // все вещи по запросу

        var result = itemRequestList.stream()
                .map(ItemRequestMapper::toItemRequestResponseDto)
                .collect(Collectors.toList());
        result = result.stream()
                .filter(item -> item.getId() != userId)
                .collect(Collectors.toList());

        for (ItemRequestResponseDto itemRequestResponseDto : result) {
            if (itemRequestResponseDto.getItems() == null) {
                itemRequestResponseDto.setItems(new ArrayList<>());
            }
            for (Item item : itemList) {
                if (Objects.equals(item.getRequest().getId(), itemRequestResponseDto.getId())) {
                    itemRequestResponseDto.getItems().add(ItemMapper.toItemRequestResponseDtoItem(item));

                }
            }
        }

        return result;
    }

    @Override
    public ItemRequestResponseDto getRequestById(Long userId, long requestId) {
        userRepository.findById(userId).orElseThrow(() -> new ObjectNotFoundException("Пользователь с id %d не найден"));
        ItemRequest itemRequest = itemRequestRepository.findById(requestId).orElseThrow(
                () -> new ObjectNotFoundException("Пользователь с id не найден"));

        List<Item> itemList = itemRepository.findItemsByRequestId(requestId); // все вещи по запросу

        ItemRequestResponseDto result = ItemRequestMapper.toItemRequestResponseDto(itemRequest);
        result.setItems(new ArrayList<>());

        for (Item item : itemList) {
            result.getItems().add(ItemMapper.toItemRequestResponseDtoItem(item));
        }

        return result;
    }


}
