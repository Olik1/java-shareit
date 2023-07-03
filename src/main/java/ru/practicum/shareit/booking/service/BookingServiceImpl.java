package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserMapper;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final Sort SORT = Sort.by(Sort.Direction.DESC, "starts");//по убыванию

    @Override
    public BookingDto addBooking(long userId, BookingDto bookingDto) {
        validateTimeBooking(bookingDto);
        var booking = BookingMapper.toBooking(bookingDto);
        var userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty()) {
            throw new ObjectNotFoundException("Пользователь не найден");
        }
        var user = userOptional.get();
        var changeItem1 = itemRepository.findAll();

        var itemOptional = itemRepository.findById(bookingDto.getItemId());
        if (itemOptional.isEmpty()) {
            throw new ObjectNotFoundException("Вещь не существует");
        }
        var item = itemOptional.get();
        if (!item.isAvailable()) {
            throw new ValidationException("Данная вещь не доступна для бронирования!");
        }
        if (item.getOwner().getId() == userId) {
            throw new ObjectNotFoundException("Зачем самому себе брать вещь в аренду! :)");
        }
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(Status.WAITING);
        log.info("Добавлна новый запрос от пользователя; {}", booking.getBooker().getName());
        var booktemp = bookingRepository.save(booking);
        var result = BookingMapper.toBookingDto(booktemp);

        result.setItem(ItemMapper.toItemDto(item));
        result.setBooker(UserMapper.toUserDto(user));

        return result;
    }

    @Override
    public BookingDto approved(long userId, long bookingId, boolean available) {

        var need = itemRepository.findAll();
        var need1 = bookingRepository.findAll();
        var booking = bookingRepository.findById(bookingId);
        if (booking.isEmpty()) {
            throw new ObjectNotFoundException("Такого бронирования не существует!");
        }

        if (booking.get().getItem().getOwner().getId() != (userId)) {
            throw new ObjectNotFoundException("id вещи пользователя не совпадают с id владелььца вещи");
        }
        Status status = booking.get().getStatus();
        if (!status.equals(Status.WAITING)) {
            throw new ValidationException("Статус нельзя изменить!");
        }
        if (available) {
            booking.get().setStatus(Status.APPROVED);
        } else {
            booking.get().setStatus(Status.REJECTED);
        }
        var result = BookingMapper.toBookingDto(bookingRepository.save(booking.get()));

        result.setItem(ItemMapper.toItemDto(booking.get().getItem()));
        result.setBooker(UserMapper.toUserDto(booking.get().getBooker()));

        return result;

    }

    @Override
    public BookingDto getBooiking(long userId, long bookingId) {
        var need = itemRepository.findAll();
        var need1 = bookingRepository.findAll();

        var booking = bookingRepository.findById(bookingId);
        if (booking.isEmpty()) {
            throw new ObjectNotFoundException("Такого бронирования не существует!");
        }

        if (booking.get().getBooker().getId() != (userId) &&
                booking.get().getItem().getOwner().getId() != (userId)) {
            throw new ObjectNotFoundException("Данные бронирования позволяет выполнять автору бронирования или владельцу вещи!");
        }

        var result = BookingMapper.toBookingDto(bookingRepository.findById(bookingId).get());

        result.setItem(ItemMapper.toItemDto(booking.get().getItem()));
        result.setBooker(UserMapper.toUserDto(booking.get().getBooker()));

        return result;
    }

    @Override
    public List<BookingDto> getItemsBookingsOfUser(long userId, State state) {

        var user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new ObjectNotFoundException("Пользователь не найден");
        }
        var need = itemRepository.findAll();
        var need1 = bookingRepository.findAll();
        List<Booking> bookings = bookingRepository.findByBooker_Id(userId);
        LocalDateTime time = LocalDateTime.now();
        switch (state) {
            case PAST:
                bookings = bookingRepository.findByBooker_IdAndEndsIsBefore(userId, time, SORT);
                break;
            case FUTURE:
                bookings = bookingRepository.findByBooker_IdAndStartsIsAfter(userId, time, SORT);
                break;
            case CURRENT:
                bookings = bookingRepository.findByBooker_IdAndStartsIsBeforeAndEndsIsAfter(userId,
                        time, time, SORT);
                break;
            case WAITING:
                bookings = bookingRepository.findByBooker_IdAndStatus(userId, Status.WAITING, SORT);
                break;
            case REJECTED:
                bookings = bookingRepository.findByBooker_IdAndStatus(userId, Status.REJECTED, SORT);
                break;
            default:
                Collections.sort(bookings, (booking1, booking2) -> booking2.getStarts().compareTo(booking1.getStarts()));
                break;
        }
        List<BookingDto> result = new ArrayList<>();
        for (Booking booking : bookings) {
            var bookingDto = BookingMapper.toBookingDto(booking);
            bookingDto.setItem(ItemMapper.toItemDto(booking.getItem()));
            bookingDto.setBooker(UserMapper.toUserDto(booking.getBooker()));
            result.add(bookingDto);
        }

        return result;
    }

    @Override
    public List<BookingDto> getBookingByItemOwner(long userId, State state) {
        var need = itemRepository.findAll();
        var need1 = bookingRepository.findAll();
        var need2 = userRepository.findAll();
        var user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new ObjectNotFoundException("Пользователь не найден");
        }
        List<Booking> bookings;
        LocalDateTime time = LocalDateTime.now();
        switch (state) {
            case PAST:
                bookings = bookingRepository.findByItemOwnerIdAndEndsIsBefore(userId, time, SORT);
                break;
            case FUTURE:

                bookings = bookingRepository.findByItemOwnerIdAndStartsIsAfter(userId, time, SORT);
                break;
            case CURRENT:
                bookings = bookingRepository.findByItemOwnerIdAndStartsIsBeforeAndEndsIsAfter(userId,
                        time, time, SORT);
                break;
            case WAITING:
                bookings = bookingRepository.findByItemOwnerIdAndStatus(userId, Status.WAITING, SORT);
                break;
            case REJECTED:
                bookings = bookingRepository.findByItemOwnerIdAndStatus(userId, Status.REJECTED, SORT);
                break;
            default:
                bookings = bookingRepository.findByItemOwnerIdOrderByStartsDesc(userId);
                break;
        }

        List<BookingDto> result = new ArrayList<>();
        for (Booking booking : bookings) {
            var bookingDto = BookingMapper.toBookingDto(booking);
            bookingDto.setItem(ItemMapper.toItemDto(booking.getItem()));
            bookingDto.setBooker(UserMapper.toUserDto(booking.getBooker()));
            result.add(bookingDto);
        }

        return result;
    }

    private void validateTimeBooking(BookingDto bookingDto) {
        if (bookingDto.getStart() == null || bookingDto.getEnd() == null) {
            throw new ValidationException("Поля не могут быть пустыми");
        }
        if (bookingDto.getStart().isEqual(bookingDto.getEnd())) {
            throw new ValidationException("Дата начала бронирования не может совпадать с датой окончания!");
        }
        if (bookingDto.getEnd().isBefore(LocalDateTime.now().minusMinutes(1))) {
            throw new ValidationException("Дата окончания бронирования не может быть в прошлом!!");
        }
        if (bookingDto.getStart().isBefore(LocalDateTime.now().minusMinutes(1))) {
            throw new ValidationException("Дата начала бронирования не может быть раньше текущего момента!");
        }
        if (bookingDto.getStart().isAfter(bookingDto.getEnd())) {
            throw new ValidationException("Дата начала бронирования не может быть позднее даты окончания бронирования!");
        }
    }

}

