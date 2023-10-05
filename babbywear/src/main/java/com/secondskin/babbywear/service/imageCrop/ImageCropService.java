package com.secondskin.babbywear.service.imageCrop;

import com.secondskin.babbywear.model.Products;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageCropService {

    void cropAndReplaceImages(Products product, List<MultipartFile> newImageFiles,
                              int x,int y,int width,int height);
}
