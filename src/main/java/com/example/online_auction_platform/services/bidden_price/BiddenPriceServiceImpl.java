package com.example.online_auction_platform.services.bidden_price;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.example.online_auction_platform.entities.BiddenPrice;
import com.example.online_auction_platform.entities.Bidder;
import com.example.online_auction_platform.entities.Product;
import com.example.online_auction_platform.services.bidder.BidderRepository;
import com.example.online_auction_platform.services.product.ProductRepository;

import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;

@Service
public class BiddenPriceServiceImpl implements BiddenPriceService {

    private BiddenPriceRepository biddenPriceRepo;
    private ProductRepository productRepo;
    private BidderRepository bidderRepo;
    public BiddenPriceServiceImpl(
        BiddenPriceRepository biddenPriceRepo,
        ProductRepository productRepo,
        BidderRepository bidderRepo
    ) {
        this.biddenPriceRepo = biddenPriceRepo;
        this.productRepo = productRepo;
        this.bidderRepo = bidderRepo;
    }

    @Override
    public List<BiddenPrice> getBiddenPriceByProductId(int productId) {
        return new ArrayList<>();
    }

    @Override
    public List<BiddenPrice> getBiddenPriceByBidderId(int bidderId) {
        return new ArrayList<>();
    }

    @Override
    public BiddenPrice add(BiddenPrice biddenPrice) {
        // check if productId exist
        if (productRepo.findById(biddenPrice.getProductId()).isEmpty()) {
            throw new RuntimeException("No such product id : " + biddenPrice.getProductId());
        }

        // check if bidderId exist
        if (bidderRepo.findByBidderId(biddenPrice.getBidderId()).isEmpty()) {
            throw new RuntimeException("No such bidder id : " + biddenPrice.getBidderId());
        }

        // check whether biddenPrice is higher than product's current price
        Product product = productRepo.findById(biddenPrice.getProductId()).get();
        Bidder bidder = bidderRepo.findByBidderId(biddenPrice.getBidderId()).get();
        int currentPrice = product.getCurrentPrice();
        int minPrice = (int) (currentPrice * 1.05);
        if (biddenPrice.getPrice() < minPrice) {
            throw new RuntimeException("The price is too low, smallest value : " + minPrice);
        }

        // check whether bidder has enough money to bid
        if (bidder.getCash() < biddenPrice.getPrice()) {
            throw new RuntimeException(
                "Not enough cash, have : " + 
                bidder.getCash() + 
                ", bid : " + 
                biddenPrice.getPrice()
            );
        }

        // bidden price is valid, save the current price of the product
        product.setCurrentPrice(biddenPrice.getPrice());
        productRepo.save(product);
        
        return biddenPriceRepo.save(biddenPrice);
    }
       
}