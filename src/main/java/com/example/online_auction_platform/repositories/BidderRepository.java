package com.example.online_auction_platform.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.online_auction_platform.entities.Bidder;
import org.springframework.stereotype.Repository;

@Repository
public interface BidderRepository extends JpaRepository<Bidder, Integer> {
    Bidder findByName(String name);
    
    // Find by exact age
    List<Bidder> findByAge(int age);
    
    // Find by age range
    List<Bidder> findByAgeBetween(int minAge, int maxAge);
    
    // Find by email (exact match)
    Bidder findByEmail(String email);
    
    // Find by email containing pattern (case-insensitive)
    List<Bidder> findByEmailContainingIgnoreCase(String emailPattern);
}
