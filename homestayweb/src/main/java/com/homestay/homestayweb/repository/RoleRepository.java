package com.homestay.homestayweb.repository;

import com.homestay.homestayweb.entity.Role;
import com.homestay.homestayweb.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}