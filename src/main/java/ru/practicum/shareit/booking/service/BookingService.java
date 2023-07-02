package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.State;

import java.util.List;

public interface BookingService {

    BookingDto addBooking(long userId, BookingDto bookingDto);

    BookingDto approved(long userId, long bookingId, boolean available);

    BookingDto getBooiking(long userId, long bookingId);

    List<BookingDto> getItemsBookingsOfUser(long userId, State state);

    List<BookingDto> getBookingByItemOwner(long userId, State state);
}
