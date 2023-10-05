package com.secondskin.babbywear.service.imageCrop;

import com.secondskin.babbywear.model.Images;
import com.secondskin.babbywear.model.Products;
import com.secondskin.babbywear.repository.ImageRepository;
import net.coobird.thumbnailator.Thumbnailator;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;


@Service
public class ImageCropServiceImpl implements ImageCropService{

    @Autowired
    ImageRepository imageRepository;


    public static String uploadDir ="C:\\Users\\ARAVIND\\IdeaProjects\\babbywear\\babbywear\\src\\main\\resources\\static\\productImages";

    @Override
    public void cropAndReplaceImages(Products product, List<MultipartFile> newImageFiles,
                                     int x, int y, int width, int height) {

        try{
            File filePath = new File(uploadDir);
            if(!filePath.exists()){
                filePath.mkdirs();
            }

            for (int i=0;i< newImageFiles.size();i++){
                MultipartFile newImage = newImageFiles.get(i);

                String uniqueFileName = System.currentTimeMillis()+"_"+newImage.getOriginalFilename();

                File croppedImage = new File(uploadDir+"/"+uniqueFileName);
                Thumbnails.of(newImage.getInputStream())
                        .sourceRegion(x,y,width,height)
                        .size(width,height).toFile(croppedImage);

                Images image = product.getImages().get(i);
                image.setImageUrl("/images"+uniqueFileName);
                imageRepository.save(image);
            }
        }
        catch (Exception e){

            e.printStackTrace();

        }



    }
}
