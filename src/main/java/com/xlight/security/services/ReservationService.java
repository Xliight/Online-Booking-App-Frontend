package com.xlight.security.services;

import com.xlight.security.model.Reservation;
import com.xlight.security.model.Room;
import com.xlight.security.Repository.ReservationRepository;
import com.xlight.security.Repository.RoomRepository;
import com.xlight.security.enums.AvailabilityStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private RoomRepository roomRepository;

    public Reservation saveReservation(Reservation reservation) {
        Optional<Room> roomOptional = roomRepository.findById(reservation.getRoom().getId());
        if (roomOptional.isEmpty()) {
            throw new RuntimeException("Room not found.");
        }

        Room room = roomOptional.get();
        if (AvailabilityStatus.BOOKED.equals(room.getAvailabilityStatus())) {
            throw new RuntimeException("Room is not available.");
        }

        // Set the room status to 'BOOKED'
        room.setAvailabilityStatus(AvailabilityStatus.BOOKED);
        roomRepository.save(room);

        return reservationRepository.save(reservation);
    }

    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id).orElse(null);
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public List<Reservation> getReservationsByUserId(Long userId) {
        return reservationRepository.findByUserId(userId);
    }

    public List<Reservation> getReservationsByRoomId(Long roomId) {
        return reservationRepository.findByRoomId(roomId);
    }

    public Reservation updateReservation(Long id, Reservation updatedReservation) {
        if (!reservationRepository.existsById(id)) {
            throw new RuntimeException("Reservation not found.");
        }

        Optional<Room> roomOptional = roomRepository.findById(updatedReservation.getRoom().getId());
        if (roomOptional.isEmpty()) {
            throw new RuntimeException("Room not found.");
        }

        Room room = roomOptional.get();
        if (AvailabilityStatus.BOOKED.equals(room.getAvailabilityStatus()) && !room.getId().equals(updatedReservation.getRoom().getId())) {
            throw new RuntimeException("Room is not available.");
        }

        // If room status was changed, update it
        if (room.getId().equals(updatedReservation.getRoom().getId())) {
            room.setAvailabilityStatus(AvailabilityStatus.BOOKED);
            roomRepository.save(room);
        }

        updatedReservation.setId(id);
        return reservationRepository.save(updatedReservation);
    }

    public void deleteReservation(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new RuntimeException("Reservation not found.");
        }
        reservationRepository.deleteById(id);
    }
}
