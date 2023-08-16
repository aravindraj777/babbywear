package com.secondskin.babbywear.repository;

import com.secondskin.babbywear.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

    Role roleName(String roleName);

    List<Role> findAll();
}
