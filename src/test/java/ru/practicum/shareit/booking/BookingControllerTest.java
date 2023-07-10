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
import ru.practicum.shareit.item.dto.ItemDto;

import static org.mockito.Mockito.*;

import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.RequestEntity.patch;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.web.servlet.function.RequestPredicates.path;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private BookingService bookingService;
    private User booker;
    private ItemDto item;
    private BookingDto bookingDto;

    private
    @BeforeEach
    @Test
    void setUpBookingDto() {
        booker = User.builder()
                .id(1L)
                .name("booker")
                .email("email")
                .build();
        item = ItemDto.builder()
                .id(1L)
                .name("item")
                .description("description")
                .owner(booker)
                .available(true)
                .build();
        bookingDto = BookingDto.builder()
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusWeeks(2))
                .itemId(1)
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


        String contentAsStrin1g = mockMvc.perform(MockMvcRequestBuilders.patch("/bookings/{bookingId}", bookingId)
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
        verify(bookingService).addBooking(booker.getId(), bookingDto);
    }


    @Test
    void getBooking() {
    }

    @Test
    void getBookingsOfUser() {
    }

    @Test
    void getBookingByItemOwner() {
    }
}