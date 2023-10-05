package com.secondskin.babbywear.repository;



import com.secondskin.babbywear.model.Order;
import com.secondskin.babbywear.model.Status;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.util.List;


@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

        Order getById(Long id);

        Page<Order> findByStatus(String status, Pageable pageable);


        List<Order> findByStatus(Status status);

        List<Order> findOrderByOrderedDateBetween(LocalDate startDate,LocalDate endDate);

        List<Order>  findByUserInfoUserName(String userName);










}
