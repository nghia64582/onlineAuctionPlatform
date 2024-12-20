package com.example.onlineAuctionPlatform.services.product;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.onlineAuctionPlatform.entities.BiddenPrice;
import com.example.onlineAuctionPlatform.entities.Product;
import com.example.onlineAuctionPlatform.services.bidden_price.BiddenPriceRepository;

@Service
public class ProductServiceImpl implements ProductService{

    private ProductRepository productRepo;
    private BiddenPriceRepository biddenPriceRepository;

    public ProductServiceImpl(
        ProductRepository productRepo,
        BiddenPriceRepository biddenPriceRepository
    ) {
        this.productRepo = productRepo;
        this.biddenPriceRepository = biddenPriceRepository;
    }

    @Override
    public List<Product> getProductByAuctioneerId(int auctioneerId) {
        return productRepo.findAll().stream().filter(product -> {
            return product.getAuctioneerId() == auctioneerId;
        }).toList();
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    @Override
    public Product getProductById(int productId) {
        Optional<Product> result = productRepo.findById(productId);
        if (result.isPresent()) {
            return result.get();
        } else {
            return null;
        }
    }

    @Override
    public Product save(Product product) {
        return productRepo.save(product);
    }

    @Override
    public int getHighestBiddenPriceByProductId(int productId) {
        List<BiddenPrice> biddenPrices = biddenPriceRepository.findAll().stream().sorted((b1, b2) -> {
            return Integer.compare(b1.getPrice(), b2.getPrice());
        }).toList();
        if (biddenPrices.size() == 0) {
            return 0;
        } else {
            return biddenPrices.get(0).getPrice();
        }
    }
    
}
