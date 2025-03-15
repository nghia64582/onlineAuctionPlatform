package com.example.online_auction_platform.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.online_auction_platform.entities.Bidder;
import com.example.online_auction_platform.repositories.BidderRepository;

@Service
public class BidderService {

    private BidderRepository bidderRepo;

    public BidderService(BidderRepository bidderRepo) {
        this.bidderRepo = bidderRepo;
    }

    public Bidder save(Bidder bidder) {
        return bidderRepo.save(bidder);
    }

    public Bidder findById(int bidderId) {
        Optional<Bidder> result = bidderRepo.findById(bidderId);
        if (result.isPresent()) {
            return result.get();
        } else {
            return null;
        }
    }

    public List<Bidder> findAll(Pageable pageable) {
        return bidderRepo.findAll(pageable).getContent();
    }
}
