package com.secondskin.babbywear.repository;

import com.secondskin.babbywear.model.Images;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface ImageRepository extends JpaRepository<Images,Long> {

    Images save(Images images);


}
