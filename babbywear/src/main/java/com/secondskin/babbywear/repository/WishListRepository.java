package com.secondskin.babbywear.repository;

import com.secondskin.babbywear.model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishListRepository extends JpaRepository<Wishlist,Long> {


}
