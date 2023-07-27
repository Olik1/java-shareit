package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final Sort sort = Sort.by(Sort.Direction.DESC, "starts");//по убыванию

    @Override
    public BookingDto addBooking(long userId, BookingDto bookingDto) {
        validateTimeBooking(bookingDto);
        var booking = BookingMapper.toBooking(bookingDto);
        var userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty()) {
            throw new ObjectNotFoundException("Пользователь не найден");
        }
        var user = userOptional.get();

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
    public List<BookingDto> getItemsBookingsOfUser(long userId, State state, int from, int size) {
        var user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new ObjectNotFoundException("Пользователь не найден");
        }
        int offset = from > 0 ? from / size : 0;
        PageRequest page = PageRequest.of(offset, size);

        List<Booking> bookings;
        Page<Booking> pageBookings;
        LocalDateTime time = LocalDateTime.now();

        pageBookings = bookingRepository.findBookingsByBooker_IdOrderByStartsDesc(userId, page);

        switch (state) {
            case PAST:
                pageBookings = bookingRepository.findByBooker_IdAndEndsIsBeforeOrderByStartsDesc(userId, time, page);
                break;
            case FUTURE:
                pageBookings = bookingRepository.findByBooker_IdAndStartsIsAfterOrderByStartsDesc(userId, time, page);
                break;
            case CURRENT:
                pageBookings = bookingRepository.findByBooker_IdAndStartsIsBeforeAndEndsIsAfterOrderByStartsDesc(userId,
                        time, time, page);
                break;
            case WAITING:
                pageBookings = bookingRepository.findByBooker_IdAndStatusOrderByStartsDesc(userId, Status.WAITING, page);
                break;
            case REJECTED:
                pageBookings = bookingRepository.findByBooker_IdAndStatusOrderByStartsDesc(userId, Status.REJECTED, page);
                break;
            default:
                break;
        }
        bookings = pageBookings.toList();


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
    public List<BookingDto> getBookingByItemOwner(long userId, State state, int from, int size) {
        var user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new ObjectNotFoundException("Пользователь не найден");
        }

        if (from < 0) {
            throw new ValidationException("Отрицательное значение фром");
        }
        int offset = from > 0 ? from / size : 0;
        PageRequest page = PageRequest.of(offset, size);


        List<Booking> bookings;
        Page<Booking> pageBookings;
        LocalDateTime time = LocalDateTime.now();

        switch (state) {
            case PAST:
                pageBookings = bookingRepository.findByItemOwnerIdAndEndsIsBeforeOrderByStartsDesc(userId, time, page);
                break;
            case FUTURE:
                pageBookings = bookingRepository.findByItemOwnerIdAndStartsIsAfterOrderByStartsDesc(userId, time, page);
                break;
            case CURRENT:
                pageBookings = bookingRepository.findByItemOwnerIdAndStartsIsBeforeAndEndsIsAfterOrderByStartsDesc(userId,
                        time, time, page);
                break;
            case WAITING:
                pageBookings = bookingRepository.findByItemOwnerIdAndStatusOrderByStartsDesc(userId, Status.WAITING, page);
                break;
            case REJECTED:
                pageBookings = bookingRepository.findByItemOwnerIdAndStatusOrderByStartsDesc(userId, Status.REJECTED, page);
                break;
            default:
                pageBookings = bookingRepository.findByItemOwnerIdOrderByStartsDesc(userId, page);
                break;
        }

        bookings = pageBookings.toList();

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

