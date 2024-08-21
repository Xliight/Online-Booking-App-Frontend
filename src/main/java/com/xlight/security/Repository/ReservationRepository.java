package com.xlight.security.Repository;

import com.xlight.security.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUserId(Long userId);
    List<Reservation> findByRoomId(Long roomId);
    Optional<Reservation> findByRoomIdAndUserEmail(Long roomId, String email);

}