package com.xlight.security.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.xlight.security.Exceptions.CustomExceptions;
import com.xlight.security.Repository.ImageRepository;
import com.xlight.security.Repository.ReservationRepository;
import com.xlight.security.Repository.RoomRepository;
import com.xlight.security.enums.AvailabilityStatus;
import com.xlight.security.model.Image;
import com.xlight.security.model.Reservation;
import com.xlight.security.model.Room;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RoomService {
    private final ReservationRepository reservationRepository;
    private final ReservationService reservationService;
    private final RoomRepository roomRepository;
    private final ImageRepository imageRepository;
    @Autowired
    private Cloudinary cloudinary;
    @Transactional
    public Room saveRoom(MultipartFile file,Room room) {
        // Ensure the room's average rating is set to 0 by default
        if (room.getId() == null) {
            room.setAverageRating(0.0); // Default value for new rooms
        }

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
        String roomnumber=room.getRoomNumber();
        Image image=uploadImage(file,roomnumber);
        room.setMainImage(image);
        // Save the room
        return roomRepository.save(room);
    }


    public Room getRoomById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new CustomExceptions.RoomNotFoundException("Room with ID " + id + " not found."));
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAllWithImages();
    }
    public List<Room> getAllAvailableRooms() {
        return roomRepository.findByAvailabilityStatus(AvailabilityStatus.AVAILABLE);
    }

    public Room updateRoom(Long id, Room updatedRoom) {
        // Check if the room exists
        Room existingRoom = roomRepository.findById(id)
                .orElseThrow(() -> new CustomExceptions.RoomNotFoundException("Room with ID " + id + " not found."));

        // Update the room details
        existingRoom.setRoomNumber(updatedRoom.getRoomNumber());
        existingRoom.setDescription(updatedRoom.getDescription());
        existingRoom.setPricePerNight(updatedRoom.getPricePerNight());
        existingRoom.setMaxAdults(updatedRoom.getMaxAdults());
        existingRoom.setMaxChildren(updatedRoom.getMaxChildren());
        existingRoom.setAvailabilityStatus(updatedRoom.getAvailabilityStatus());

        // Save the updated room
        return roomRepository.save(existingRoom);
    }

    public void deleteRoom(Long id) {
        // Check if the room exists
        if (!roomRepository.existsById(id)) {
            throw new CustomExceptions.RoomNotFoundException("Room with ID " + id + " not found.");
        }

        // Fetch all reservations associated with the room
        List<Reservation> reservations = reservationRepository.findByRoomId(id);

        // Delete all associated reservations using the deleteReservation method
        for (Reservation reservation : reservations) {
            reservationService.deleteReservation(reservation.getId()); // Use the service method to delete
        }

        // Delete the room
        roomRepository.deleteById(id);
    }

    public Image uploadImage(MultipartFile file, String roomnumber) {
        try {
//            Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
//                    "folder", folder,
//                    "public_id", publicId
//            ));
            String folderPath = String.format("rooms/room\"%s\"", roomnumber);

            String originalFileName = file.getOriginalFilename();
            String imageName = originalFileName != null ? originalFileName.replaceFirst("[.][^.]+$", "") : "default_image_name";
            Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "folder", "rooms/"+roomnumber,
                    "public_id",imageName,
                    "use_filename",true,
                    "unique_filename",false
            ));

            String url = (String) uploadResult.get("url");
            String cloudinaryPublicId = (String) uploadResult.get("public_id");

            Image image = new Image();
            image.setUrl(url);
            image.setPublicId(cloudinaryPublicId);
            return imageRepository.save(image);
        } catch (IOException e) {
            throw new RuntimeException("upload fail"+ e.getMessage());
        }

    }


}
