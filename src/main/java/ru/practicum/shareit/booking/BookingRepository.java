package ru.practicum.shareit.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDateTime;
import java.util.List;

//@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBooker_IdAndEndsIsBefore(Long bookerId, LocalDateTime ends, Sort sort);
    Page<Booking> findByBooker_IdAndEndsIsBeforeOrderByStartsDesc(Long bookerId, LocalDateTime ends, Pageable pageable);

    List<Booking> findByBooker_IdAndStartsIsAfter(Long bookerId, LocalDateTime starts, Sort sort);
    Page<Booking> findByBooker_IdAndStartsIsAfterOrderByStartsDesc(Long bookerId, LocalDateTime starts,  Pageable pageable);

    List<Booking> findByBooker_IdAndStatus(Long bookerId, Status status, Sort sort);
    Page<Booking> findByBooker_IdAndStatusOrderByStartsDesc(Long bookerId, Status status, Pageable pageable);

    List<Booking> findByBooker_IdAndStartsIsBeforeAndEndsIsAfter(Long bookerId, LocalDateTime starts,
                                                                 LocalDateTime ends, Sort sort);
    Page<Booking> findByBooker_IdAndStartsIsBeforeAndEndsIsAfterOrderByStartsDesc(Long bookerId, LocalDateTime starts,
                                                                 LocalDateTime ends, Pageable pageable);

    List<Booking> findByItemOwnerIdAndEndsIsBefore(Long bookerId, LocalDateTime ends, Sort sort);

    List<Booking> findByItemOwnerIdAndStartsIsAfter(Long bookerId, LocalDateTime starts, Sort sort);

    List<Booking> findByItemOwnerIdAndStatus(Long bookerId, Status status, Sort sort);

    List<Booking> findByItemOwnerIdAndStartsIsBeforeAndEndsIsAfter(Long bookerId, LocalDateTime starts,
                                                                   LocalDateTime ends, Sort sort);

    Page<Booking> findByItemOwnerIdAndEndsIsBeforeOrderByStartsDesc(Long bookerId, LocalDateTime ends, Pageable pageable);

    Page<Booking> findByItemOwnerIdAndStartsIsAfterOrderByStartsDesc(Long bookerId, LocalDateTime starts, Pageable pageable);

    Page<Booking> findByItemOwnerIdAndStatusOrderByStartsDesc(Long bookerId, Status status, Pageable pageable);

    Page<Booking> findByItemOwnerIdAndStartsIsBeforeAndEndsIsAfterOrderByStartsDesc(Long bookerId, LocalDateTime starts,
                                                                   LocalDateTime ends, Pageable pageable);

    List<Booking> findByBooker_Id(Long bookerId);

    List<Booking> findBookingByItem_Id(Long itemId);

    List<Booking> findBookingByItem_IdAndStatus(Long itemId, Status status);

    List<Booking> findByItemOwnerIdOrderByStartsDesc(Long ownerId);
    Page<Booking> findByItemOwnerIdOrderByStartsDesc(Long ownerId, Pageable pageable );
//    Page<Booking> findByOrderByCreatedDesc(Pageable pageable);
    Page<Booking> findBookingsByBooker_IdOrderByStartsDesc(long bookerId, Pageable pageable);


}
