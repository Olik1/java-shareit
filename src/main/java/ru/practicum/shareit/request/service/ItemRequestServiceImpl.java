package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

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
        var itemRequests = itemRequestRepository.findItemRequestsByRequesterId(userId);

//        List<Long> requestIds = new ArrayList<>(); //id item чтобы понять какие вещи добавлены
//        for (ItemRequest itemRequest : itemRequests) {
//            requestIds.add(itemRequest.getId());
//        }
        List<Long> requestIds = itemRequests.stream()
                .map(ItemRequest::getId)
                .collect(Collectors.toList());
        List<Item> itemList = itemRepository.findItemsByRequestIdIn(requestIds); // все вещи по запросу
//        List<ItemRequestResponseDto> result = new ArrayList<>();
//        for (ItemRequest itemRequest : itemRequests) {
//            result.add(ItemRequestMapper.toItemRequestResponseDto(itemRequest));
//        }
        List<ItemRequestResponseDto> result = itemRequests.stream()
                .map(ItemRequestMapper::toItemRequestResponseDto)
                .collect(Collectors.toList());

        for (ItemRequestResponseDto itemRequestResponseDto : result) {
            for (Item item : itemList) {
                if (Objects.equals(item.getRequest().getId(), itemRequestResponseDto.getId())) {
                    if (itemRequestResponseDto.getItems().isEmpty()) {
                        itemRequestResponseDto.setItems(new ArrayList<>());
                    }
                    itemRequestResponseDto.getItems().add(ItemMapper.toItemRequestResponseDtoItem(item));

                }
            }
        }

        return result;
    }

    @Override
    public List<ItemRequestDto> getAllRequests(long userId, int from, int size) {
        //Для этого используйте метод PageRequest.of(page, size, sort) .
        PageRequest page = PageRequest.of(from / size, size);
        List<ItemRequest> itemRequestList = itemRequestRepository.findAllByRequester_IdNotOrderByCreatedDesc(userId,
                page);
        return itemRequestList.stream()
                .map(ItemRequestMapper::toItemRequesDto)
                .collect(Collectors.toList());
    }


}
