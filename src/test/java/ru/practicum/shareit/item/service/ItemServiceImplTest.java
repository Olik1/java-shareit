package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
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
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserService userService;
    @Mock
    private CommentRepository commentRepository;
    @InjectMocks
    private ItemServiceImpl itemService;
    private User owner;
    private UserDto ownerDto;
    private User booker;
    private ItemDto itemDto;
    private BookingDto booking;
    private Comment comment;

    @BeforeEach
    void setUser() {
        owner = User.builder()
                .id(2L)
                .name("owner")
                .email("email2@email.com")
                .build();

        booker = User.builder()
                .id(1L)
                .name("booker")
                .email("email2@email.com")
                .build();
        itemDto = ItemDto.builder()
                .id(1L)
                .name("item")
                .description("description")
                .owner(owner)
                .available(true)
                .build();
        booking = BookingDto.builder()
                .id(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusWeeks(2))
                .bookerId(1L)
                .itemId(1L)
                .build();

        ownerDto = UserMapper.toUserDto(owner);
        comment = Comment.builder()
                .id(1L)
                .text("comment1")
                .author(booker)
                .item(ItemMapper.toItem(itemDto))
                .build();

    }

    @Test
    void addItem() {
        long userId = owner.getId();

        Item item = ItemMapper.toItem(itemDto);
        when(userService.getUserById(userId)).thenReturn(ownerDto);
        item.setOwner(UserMapper.toUser(ownerDto));
        when(itemRepository.save(item)).thenReturn(item);

        ItemDto actualItemDto = itemService.addItem(userId, itemDto);

        assertNotNull(itemDto);
        assertEquals(actualItemDto.getId(), 1L);
        assertEquals(actualItemDto.getName(), "item");
        verify(itemRepository).save(item);

    }

    @Test
    void updateItem() {
        long userId = owner.getId();

        Item item = ItemMapper.toItem(itemDto);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        item.setOwner(UserMapper.toUser(ownerDto));
        when(itemRepository.save(item)).thenReturn(item);

        ItemDto actualItemDto = itemService.updateItem(userId, itemDto);

        assertNotNull(itemDto);
        assertEquals(actualItemDto.getId(), 1L);
        assertEquals(actualItemDto.getName(), "item");
        verify(itemRepository).save(item);

    }

    @Test
    void getItem() {
        // Arrange
        long itemId = itemDto.getId();
        long userId = owner.getId();

        List<Comment> comments = new ArrayList<>();
        var item = ItemMapper.toItem(itemDto);
        item.setOwner(owner);
        comments.add(comment);
        List<Booking> bookings = new ArrayList<>();

        var booking1 = BookingMapper.toBooking(booking);
        booking1.setItem(item);
        booking1.setBooker(booker);
        bookings.add(booking1);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(commentRepository.findCommentsByItem_Id(itemId)).thenReturn(comments);
        when(bookingRepository.findBookingByItem_IdAndStatus(itemId, Status.APPROVED)).thenReturn(bookings);

        ItemDto itemDto = itemService.getItem(itemId, userId);
        verify(itemRepository).findById(itemId);

        assertNotNull(itemDto);
        assertEquals(itemId, itemDto.getId());
        assertEquals(comment.getId(), itemDto.getComments().get(0).getId());
    }

    @Test
    void addComment() {
        long itemId = itemDto.getId();
        long userId = booker.getId();

        var item = ItemMapper.toItem(itemDto);
        item.setOwner(owner);

        List<Booking> bookings = new ArrayList<>();
        var booking1 = BookingMapper.toBooking(booking);
        booking1.setItem(item);
        booking1.setBooker(booker);
        booking1.setStatus(Status.APPROVED);
        bookings.add(booking1);

        comment.setItem(item);
        comment.setCreated(LocalDateTime.now());

        var commentDto = CommentMapper.tocommentDto(comment);

        when(itemRepository.findById(eq(itemId))).thenReturn(Optional.of(item));
        when(userRepository.findById(eq(userId))).thenReturn(Optional.of(booker));
        when(bookingRepository.findBookingByItem_Id(eq(itemId))).thenReturn(bookings);
        doReturn(comment).when(commentRepository).save(any(Comment.class));

        CommentDto commentTest = itemService.addComment(userId, itemId, commentDto);

        assertThat(commentTest, notNullValue());
        assertThat(commentTest.getId(), equalTo(comment.getId()));
        verify(userRepository).findById(userId);
        verify(bookingRepository).findBookingByItem_Id(itemId);
        verify(commentRepository).save(any());
        verifyNoMoreInteractions(userRepository, itemRepository, bookingRepository, commentRepository);

    }
}