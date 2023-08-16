package com.secondskin.babbywear.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductDto {

    private List<MultipartFile> productImages = new ArrayList<>();
}
