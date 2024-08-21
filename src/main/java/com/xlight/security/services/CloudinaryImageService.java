package com.xlight.security.services;

import com.xlight.security.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface CloudinaryImageService {
     Map upload(MultipartFile file);

     List<Image> uploadImages(List<MultipartFile> file, String folder);
}
