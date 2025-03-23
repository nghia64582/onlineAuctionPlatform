package com.example.online_auction_platform.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.online_auction_platform.dto.request.product.AddProductReqDto;
import com.example.online_auction_platform.entities.Product;
import com.example.online_auction_platform.entities.SoldProduct;
import com.example.online_auction_platform.repositories.ProductRepository;
import com.example.online_auction_platform.repositories.SoldProductRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductService {

    private ProductRepository productRepo;
    private SoldProductRepository soldProductRepo;
    private ImageService imageService;

    public List<Product> findProductByAuctioneerId(int auctioneerId) {
        return productRepo.findByAuctioneer_Id(auctioneerId, PageRequest.of(0, 20)).getContent();
    }

    public List<Product> findAllOpenProducts(
        Pageable pageable,
        List<String> categories
    ) {
        System.out.println("ProductServiceImpl.getAllProducts");
        return productRepo.findAll(pageable).getContent();
    }

    public Product findById(int productId) {
        Optional<Product> result = productRepo.findById(productId);
        if (result.isPresent()) {
            return result.get();
        } else {
            return null;
        }
    }

    public Product save(Product product) {
        return productRepo.save(product);
    }

    public int findHighestBiddenPriceByProductId(int productId) {
        // todo
        return 0;
    }

    public Product findByImageUrl(String imageUrl) {
        Optional<Product> result = productRepo.findByImageUrl(imageUrl);
        return result.get();
    }

    public Product addNewProduct(AddProductReqDto addProductDto) {
        
        // 1. Save image
        String imagePath;
        try {
            imagePath = imageService.addImage(addProductDto.getImage());
        } catch (Exception e) {
            throw new RuntimeException("Image is null.");
        }

        // 2. Process product data
        Product product = Product.builder()
            .name(addProductDto.getName())
            .location(addProductDto.getLocation())
            .beginningPrice(addProductDto.getPrice())
            .currentPrice(addProductDto.getPrice())
            .imageUrl(imagePath).build();

        save(product);
        return product;
    }

    public List<Product> findByAuctioneerId(int auctioneerId, Pageable pageable) {
        List<Product> result = productRepo.findByAuctioneerWithCategories(auctioneerId, pageable);
        result.forEach(product -> product.setBiddenPrices(List.of()));
        return result;
    }

    public List<Product> findBiddingProducts(int bidderId, Pageable pageable) {
        List<Product> result = productRepo.findBiddingProductByBidderId(bidderId, pageable);
        result.forEach(product -> product.setBiddenPrices(List.of()));
        return result;
    }

    public List<SoldProduct> findSoldProductByBidderId(int bidderId, Pageable pageable) {
        List<SoldProduct> result = soldProductRepo.findByBidder_Id(bidderId, pageable);
        result.forEach(product -> product.setBiddenPrices(List.of()));
        return result;
    }

    public boolean updateProductInfo(
        int auctioneerId,
        int productId
    ) {
        return true;
    }

    public boolean deleteProduct(
        int auctioneerId,
        int productId
    ) {
        return true;
    }

    /**
     * Find products by category IDs that come as strings
     * @param categoryIdStrings List of category IDs as strings
     * @return List of products matching the category IDs
     * @throws IllegalArgumentException if any ID is not a valid integer
     */
    public List<Product> findProductsByCategoryIdStrings(List<String> categoryIdStrings) {
        try {
            List<Integer> categoryIds = categoryIdStrings.stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());
            return productRepo.findByCategories_IdIn(categoryIds);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid category ID format. All IDs must be valid integers.");
        }
    }

    /**
     * Find products by category IDs with validation
     * @param categoryIds List of category IDs as integers
     * @return List of products matching the category IDs
     */
    public List<Product> findProductsByCategoryIds(List<Integer> categoryIds) {
        return productRepo.findByCategories_IdIn(categoryIds);
    }
}
