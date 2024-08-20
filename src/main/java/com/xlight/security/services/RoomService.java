package com.xlight.security.services;

import com.xlight.security.Repository.RoomRepository;
import com.xlight.security.model.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public Room saveRoom(Room room) {
        // Check if the room already exists by ID (for updating scenarios)
        if (room.getId() != null) {
            Optional<Room> existingRoom = roomRepository.findById(room.getId());
            if (existingRoom.isPresent() && !existingRoom.get().getRoomNumber().equals(room.getRoomNumber())) {
                throw new RuntimeException("Room with this ID already exists.");
            }
        }

        // Check if a room with the same room number already exists
        if (roomRepository.findByRoomNumber(room.getRoomNumber()).isPresent()) {
            throw new RuntimeException("Room with this number already exists.");
        }

        // Save the new room
        return roomRepository.save(room);
    }

    public Room getRoomById(Long id) {
        return roomRepository.findById(id).orElse(null);
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Room updateRoom(Room room) {
        return roomRepository.save(room);
    }

    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }
}
