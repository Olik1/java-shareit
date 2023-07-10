package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingService bookingService;
    private User booker;
    private User owner;
    private ItemDto item;
    private BookingDto bookingDto;



//    @BeforeEach
//    @Test
//    void setItemDto() {
//        itemDto = ItemDto.builder()
//                .name("name")
//                .description("description")
//                .available(true)
//                .build();
//    }

    private
    @BeforeEach
    @Test
    void setUpBookingDto() {
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
        item = ItemDto.builder()
                .id(1L)
                .name("item")
                .description("description")
                .owner(owner)
                .available(true)
                .build();
        bookingDto = BookingDto.builder()
                .id(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusWeeks(2))
                .bookerId(1L)
                .itemId(1L)
                .build();
    }


    @SneakyThrows
    @Test
    void addBookingRequest() {
        when(bookingService.addBooking(anyLong(), any(BookingDto.class))).thenReturn(bookingDto);

        String contentAsString = mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", booker.getId())
                        .content(objectMapper.writeValueAsString(bookingDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(objectMapper.writeValueAsString(bookingDto), contentAsString);
        verify(bookingService).addBooking(booker.getId(), bookingDto);
    }



    @SneakyThrows
    @Test
    void approvedBookingRequest() {
        bookingDto.setStatus(Status.APPROVED);
        when(bookingService.approved(anyLong(), anyLong(), anyBoolean())).thenReturn(bookingDto);
        var bookingId = bookingDto.getId();
        var userId = booker.getId();


        String contentAsString = mockMvc.perform(MockMvcRequestBuilders.patch("/bookings/{bookingId}", bookingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", booker.getId())
                        .param("approved", "true")
                        .content(objectMapper.writeValueAsString(bookingDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(objectMapper.writeValueAsString(bookingDto), contentAsString);
        verify(bookingService).approved(userId,bookingId, true);
    }


}