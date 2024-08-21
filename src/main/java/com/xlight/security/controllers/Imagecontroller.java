package com.xlight.security.controllers;

import com.xlight.security.model.Image;
import com.xlight.security.services.CloudinaryImageServiceImpl;
import com.xlight.security.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/images")
public class Imagecontroller {

    @Autowired
    private RoomService roomService;

    @Autowired
    private CloudinaryImageServiceImpl cloudinaryImageService;
    @GetMapping("/getimagebyid/{id}")
    public ResponseEntity<Image> getImageById(@PathVariable Long id) {
        try {
            Image image = cloudinaryImageService.getImageById(id);
            return ResponseEntity.ok(image);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/getallimages")
    public ResponseEntity<List<Image>> getAllImages() {
        List<Image> images = cloudinaryImageService.getAllImages();
        return ResponseEntity.ok(images);
    }

    @DeleteMapping("/deleteimagebyID/{id}")
    public ResponseEntity<String> deleteImage(@PathVariable Long id) {
        try {
            cloudinaryImageService.deleteImage(id);
            return ResponseEntity.ok("Image with ID " + id + " has been successfully deleted.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete image: " + e.getMessage());
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Image with ID " + id + " not found.");
        }
    }
}
