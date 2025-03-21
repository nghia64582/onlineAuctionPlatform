package com.example.online_auction_platform.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.online_auction_platform.entities.SoldProduct;
import org.springframework.stereotype.Repository;

@Repository
public interface SoldProductRepository extends JpaRepository<SoldProduct, Integer> {
    
    public List<SoldProduct> findByBidder_Id(int bidderId, Pageable pageable);
}
