package com.example.viegymapp.repository;

import java.lang.ScopedValue;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.viegymapp.entity.User;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUserName(String userName);

    Boolean existsByEmail(String email);

}
