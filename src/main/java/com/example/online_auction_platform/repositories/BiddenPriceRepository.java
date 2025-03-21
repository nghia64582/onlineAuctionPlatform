package com.example.online_auction_platform.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.online_auction_platform.entities.BiddenPrice;
import org.springframework.stereotype.Repository;

@Repository
public interface BiddenPriceRepository extends JpaRepository<BiddenPrice, Integer> {
    public Page<BiddenPrice> findAll(Pageable pageable);
}
