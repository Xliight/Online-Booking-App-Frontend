package com.xlight.security.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.xlight.security.Repository.ImageRepository;
import com.xlight.security.Repository.RoomRepository;
import com.xlight.security.model.Image;
import com.xlight.security.model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CloudinaryImageServiceImpl implements CloudinaryImageService{
    @Autowired
    private Cloudinary cloudinary;
    @Autowired
    private  ImageRepository imageRepository;
    @Autowired
    private  RoomRepository roomRepository;

    @Override
    public Map upload(MultipartFile file) {
        try {
            Map data=this.cloudinary.uploader().upload(file.getBytes(),Map.of());
            return data;
        }catch (IOException e){
            throw new RuntimeException("image uploading fail");

        }

    }

    // Method to upload an image
//    public Image uploadImage(MultipartFile file,String roomnumber) {
//        try {
//            Optional<Room> roomOptional = roomRepository.findByRoomNumber(roomnumber);
//
//            if (!roomOptional.isPresent()) {
//                throw new RuntimeException("Room with number " + roomnumber + " does not exist.");
//            }
//            String originalFileName = file.getOriginalFilename();
//            String imageName = originalFileName != null ? originalFileName.replaceFirst("[.][^.]+$", "") : "default_image_name";
//            Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
//                    "folder", "rooms/"+roomnumber,
//                    "public_id",imageName,
//                    "use_filename",true,
//                    "unique_filename",false
//            ));
//
//            String url = (String) uploadResult.get("url");
//            String cloudinaryPublicId = (String) uploadResult.get("public_id");
//
//            Image image = new Image();
//            image.setUrl(url);
//            image.setPublicId(cloudinaryPublicId);
//            return imageRepository.save(image);
//        } catch (IOException e) {
//            throw new RuntimeException("upload fail"+ e.getMessage());
//        }
//
//    }

    public List<Image> uploadImages(List<MultipartFile> files, String roomNumber) {
        List<Image> uploadedImages = new ArrayList<>();
        Room room = roomRepository.findByRoomNumber(roomNumber)
                .orElseThrow(() -> new RuntimeException("Room with number " + roomNumber + " does not exist."));

        for (MultipartFile file : files) {
            try {
                String originalFileName = file.getOriginalFilename();
                String imageName = originalFileName != null ? originalFileName.replaceFirst("[.][^.]+$", "") : "default_image_name";

                Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                        "folder", "rooms/" + roomNumber,
                        "public_id", imageName,
                        "use_filename", true,
                        "unique_filename", false
                ));

                String url = (String) uploadResult.get("url");
                String cloudinaryPublicId = (String) uploadResult.get("public_id");

                Image image = new Image();
                image.setUrl(url);
                image.setPublicId(cloudinaryPublicId);
                image.setRoom(room); // Associate the image with the room

                uploadedImages.add(imageRepository.save(image)); // Save and add to the list of uploaded images

            } catch (IOException e) {
                throw new RuntimeException("Upload failed for file: " + file.getOriginalFilename() + " due to " + e.getMessage());
            }
        }

        return uploadedImages;
    }










    public Image getImageById(Long id) {
        return imageRepository.findById(id).orElseThrow(() -> new RuntimeException("Image not found"));
    }

    public List<Image> getAllImages() {
        return imageRepository.findAll();
    }

//    public void deleteImage(Long id) throws IOException {
//        Image image = getImageById(id);
//        Room room = image.getRoom();
//
//
//        if (room != null) {
//            room.getImages().remove(image);
//            roomRepository.save(room); // Save room to update the image list
//        }
//
//
//        cloudinary.uploader().destroy(image.getPublicId(), ObjectUtils.emptyMap());
//        imageRepository.deleteById(id);
//    }
//

    public void deleteImage(Long id) throws IOException {
        roomRepository.removeMainImageFromRoom(id);
    }
}
