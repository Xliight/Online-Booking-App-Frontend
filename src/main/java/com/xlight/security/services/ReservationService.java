package com.xlight.security.services;

import com.xlight.security.Exceptions.CustomExceptions;
import com.xlight.security.Repository.UserRepository;
import com.xlight.security.model.Reservation;
import com.xlight.security.model.Room;
import com.xlight.security.Repository.ReservationRepository;
import com.xlight.security.Repository.RoomRepository;
import com.xlight.security.enums.AvailabilityStatus;
import com.xlight.security.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@RequiredArgsConstructor
@Service
public class ReservationService {


    private final ReservationRepository reservationRepository;

    private final  UserRepository userRepository;
    private final RoomRepository roomRepository;

    public Reservation saveReservation(Reservation reservation) {
        // Fetch user and room by their IDs
        User user = userRepository.findById(reservation.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + reservation.getUser().getId()));
        Room room = roomRepository.findById(reservation.getRoom().getId())
                .orElseThrow(() -> new RuntimeException("Room not found with ID: " + reservation.getRoom().getId()));

        // Check room availability
        if (AvailabilityStatus.BOOKED.equals(room.getAvailabilityStatus())) {
            throw new RuntimeException("Room is not available.");
        }

        // Set the room status to 'BOOKED'
        room.setAvailabilityStatus(AvailabilityStatus.BOOKED);
        roomRepository.save(room);

        // Set user and room to the reservation
        reservation.setUser(user);
        reservation.setRoom(room);

        // Save the reservation
        return reservationRepository.save(reservation);
    }

    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new CustomExceptions.ReservationNotFoundException("Reservation not found with ID: " + id));
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
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new CustomExceptions.ReservationNotFoundException("Reservation not found with ID: " + id));

        Room room = reservation.getRoom();

        if (room != null) {
            room.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
            roomRepository.save(room);
        } else {
            throw new RuntimeException("Associated room not found for reservation with ID: " + id);
        }

        reservationRepository.delete(reservation);
    }


}
