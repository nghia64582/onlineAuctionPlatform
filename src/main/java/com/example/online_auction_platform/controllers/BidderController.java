package com.example.online_auction_platform.controllers;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.online_auction_platform.dto.request.bidder.GetBiddersReqDto;
import com.example.online_auction_platform.entities.Bidder;
import com.example.online_auction_platform.services.BidderService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/bidder")
@AllArgsConstructor
public class BidderController {

    private BidderService bidderService;

    @PutMapping("/")
    public Bidder updateInfo(@RequestBody Bidder bidder) {
        Bidder dbBidder = bidderService.findById(bidder.getId());
        if (dbBidder == null) {
            throw new RuntimeException("No valid bidder with id : " + bidder.getId());
        }
        dbBidder.setUsername(bidder.getUsername());
        dbBidder.setEmail(bidder.getEmail());
        dbBidder.updateCreatedDate();

        bidderService.save(dbBidder);
        return dbBidder;
    }

    @GetMapping("/{bidderId}")
    public Bidder getBidderById(@PathVariable int bidderId) {
        Bidder bidder = bidderService.findById(bidderId);
        if (bidder == null) {
            throw new RuntimeException("Not found bidder id : " + bidderId);
        }
        bidder.setBoughtProducts(List.of());
        return bidder;
    }

    @GetMapping("/")
    public List<Bidder> getListBidder(@RequestBody GetBiddersReqDto req) {
        PageRequest pageRequest = PageRequest.of(req.getPageNumber(), req.getPageSize());
        List<Bidder> bidders = bidderService.findAll(pageRequest);
        return bidders;
    }

}
