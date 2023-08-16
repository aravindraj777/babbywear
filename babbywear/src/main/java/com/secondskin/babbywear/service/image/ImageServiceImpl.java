package com.secondskin.babbywear.service.image;

import com.secondskin.babbywear.model.Images;
import com.secondskin.babbywear.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageServiceImpl implements ImageService{

    @Autowired
    ImageRepository imageRepository;
    @Override
    public Images saveImages(Images images) {
        return imageRepository.save(images) ;
    }
}
