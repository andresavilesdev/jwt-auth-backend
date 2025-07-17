package com.andresavilesdev.demo_jwt.security.user.repository;

import com.andresavilesdev.demo_jwt.security.user.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<UserEntity,Long> {

    Optional<UserEntity> findByUsername(String username);

}
