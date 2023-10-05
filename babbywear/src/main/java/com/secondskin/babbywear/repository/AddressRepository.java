package com.secondskin.babbywear.repository;

import com.secondskin.babbywear.model.Address;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AddressRepository extends JpaRepository<Address,Long> {


}
