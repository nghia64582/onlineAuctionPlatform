package com.example.online_auction_platform.repositories;

import java.util.Optional;

import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.online_auction_platform.entities.User;

@Primary
public interface UserRepository extends JpaRepository<User, Integer> {

    public Optional<User> findByUsername(String userName);

    public Page<User> findAll(Pageable pageable);
    
}
