package com.ecommerce.library.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class ImageUpload {
    private final String UPDLOAD_FOLDER = "D:\\User\\Admin\\IdeaProjects\\Ecommerce\\Admin\\src\\main\\resources\\static\\img\\image-product";

    public boolean uploadImage(MultipartFile imageProduct){
        boolean isUpload = false;
        try{
            Files.copy(imageProduct.getInputStream(),
                    Paths.get(UPDLOAD_FOLDER + File.separator,imageProduct.getOriginalFilename()),
                    StandardCopyOption.REPLACE_EXISTING);
            isUpload=true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return isUpload;
    }

    public boolean checkExisted(MultipartFile imageProduct){
        boolean isExisted = false;
        try{
            File file = new File(UPDLOAD_FOLDER + "\\" + imageProduct.getOriginalFilename());
            isExisted = file.exists();
        }catch(Exception e){
            e.printStackTrace();
        }
        return isExisted;
    }
}
