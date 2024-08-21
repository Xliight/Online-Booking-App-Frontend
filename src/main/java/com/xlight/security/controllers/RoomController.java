package com.xlight.security.controllers;

import com.xlight.security.Exceptions.CustomExceptions;
import com.xlight.security.model.Image;
import com.xlight.security.model.Room;
import com.xlight.security.services.CloudinaryImageService;
import com.xlight.security.services.CloudinaryImageServiceImpl;
import com.xlight.security.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rooms/")
public class RoomController {
    @Autowired
    private CloudinaryImageService cloudinaryImageService;
    @Autowired
    private RoomService roomService;



    @PostMapping("/addroom")
    public ResponseEntity<?> createRoom( @RequestPart("file") MultipartFile file,
                                         @RequestPart("room") Room room) {
        try {
            Room savedRoom = roomService.saveRoom(file,room);
            return ResponseEntity.ok(savedRoom);
        } catch (RuntimeException ex) {
            // Handle known exceptions (e.g., room number already exists)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error creating room: " + ex.getMessage());
        } catch (Exception ex) {
            // Handle unexpected exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + ex.getMessage());
        }
    }


    @GetMapping("/getRoomById/{id}")
    public ResponseEntity<?> getRoomById(@PathVariable Long id) {
        try {
            Room room = roomService.getRoomById(id);
            return ResponseEntity.ok(room);
        } catch (CustomExceptions.RoomNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Room not found: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving room: " + ex.getMessage());
        }
    }

    @GetMapping("/getAllRooms")
    public ResponseEntity<?> getAllRooms() {
        try {
            List<Room> rooms = roomService.getAllRooms();
            return ResponseEntity.ok(rooms);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching rooms: ");
        }
    }
    @GetMapping("/getAllAvailableRooms")
    public ResponseEntity<List<Room>> getAllAvailableRooms() {
        List<Room> availableRooms = roomService.getAllAvailableRooms();
        return ResponseEntity.ok(availableRooms);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateRoom(@PathVariable Long id, @RequestBody Room room) {
        try {
            Room updatedRoom = roomService.updateRoom(id, room);
            return ResponseEntity.ok(updatedRoom);
        } catch (CustomExceptions.RoomNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating room: " + ex.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteRoom(@PathVariable Long id) {
        try {
            roomService.deleteRoom(id);
            return ResponseEntity.ok("Room with ID " + id + " has been successfully deleted.");
        } catch (CustomExceptions.RoomNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Room with ID " + id + " not found.");
        }
    }

    @PostMapping("/uploadimagetoroom")
    public ResponseEntity<?> uploadImage(
            @RequestParam("image") List<MultipartFile> file,
            @RequestParam("roomNumber") String roomNumber) {
        try {
            List<Image> image = cloudinaryImageService.uploadImages(file, roomNumber);
            return new ResponseEntity<>(image, HttpStatus.CREATED);
        } catch (RuntimeException ex) {
            // Return a response with the error message
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

}
