package com.example.online_auction_platform.services.bidder;

import com.example.online_auction_platform.entities.Bidder;

public interface BidderService {
     
    Bidder addByUsername(String username);

    Bidder save(Bidder bidder);

    Bidder findById(int id);

    int calcUserByNameInRange(String method, String name, int min, int max);
}
