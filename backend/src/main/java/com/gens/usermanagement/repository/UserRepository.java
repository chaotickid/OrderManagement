package com.gens.usermanagement.repository;

import com.gens.usermanagement.model.document.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    Optional<User> findByCustomerId(String customerId);
}