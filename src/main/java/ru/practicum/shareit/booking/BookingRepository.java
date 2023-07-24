package ru.practicum.shareit.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Page<Booking> findByBooker_IdAndEndsIsBeforeOrderByStartsDesc(Long bookerId, LocalDateTime ends, Pageable pageable);

    Page<Booking> findByBooker_IdAndStartsIsAfterOrderByStartsDesc(Long bookerId, LocalDateTime starts, Pageable pageable);

    Page<Booking> findByBooker_IdAndStatusOrderByStartsDesc(Long bookerId, Status status, Pageable pageable);

    Page<Booking> findByBooker_IdAndStartsIsBeforeAndEndsIsAfterOrderByStartsDesc(Long bookerId, LocalDateTime starts,
                                                                                  LocalDateTime ends, Pageable pageable);

    Page<Booking> findByItemOwnerIdAndEndsIsBeforeOrderByStartsDesc(Long bookerId, LocalDateTime ends, Pageable pageable);

    Page<Booking> findByItemOwnerIdAndStartsIsAfterOrderByStartsDesc(Long bookerId, LocalDateTime starts, Pageable pageable);

    Page<Booking> findByItemOwnerIdAndStatusOrderByStartsDesc(Long bookerId, Status status, Pageable pageable);

    Page<Booking> findByItemOwnerIdAndStartsIsBeforeAndEndsIsAfterOrderByStartsDesc(Long bookerId, LocalDateTime starts,
                                                                                    LocalDateTime ends, Pageable pageable);

    List<Booking> findBookingByItem_Id(Long itemId);

    List<Booking> findBookingByItem_IdAndStatus(Long itemId, Status status);

    Page<Booking> findByItemOwnerIdOrderByStartsDesc(Long ownerId, Pageable pageable);

    Page<Booking> findBookingsByBooker_IdOrderByStartsDesc(Long bookerId, Pageable pageable);
}