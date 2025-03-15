package com.example.online_auction_platform.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.online_auction_platform.dto.request.product.GetSellingProductReqDto;
import com.example.online_auction_platform.dto.request.product.GetSoldProductsByBidderReqDto;
import com.example.online_auction_platform.dto.request.product.UpdateProductRequestDto;
import com.example.online_auction_platform.dto.request.product.AddProductReqDto;
import com.example.online_auction_platform.dto.request.product.DeleteProductReqDto;
import com.example.online_auction_platform.dto.request.product.GetBiddingProductReqDto;
import com.example.online_auction_platform.dto.request.product.GetOpenProductReqDto;
import com.example.online_auction_platform.dto.request.product.GetProductReqDto;
import com.example.online_auction_platform.entities.Product;
import com.example.online_auction_platform.entities.SoldProduct;
import com.example.online_auction_platform.services.ProductService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@AllArgsConstructor
@RequestMapping("/product")
public class ProductController {
    
    ProductService productService;
    
    Gson gson = new GsonBuilder().create();
    
    /*
     * Get selling product of an auctioneer
     */
    @GetMapping("/selling")
    public List<Product> getSellingProduct(
        @RequestBody GetSellingProductReqDto req
    ) {
        System.out.println("ProductController.getProduct");
        List<Product> result = productService.findByAuctioneerId(
            req.getAuctioneerId(), 
            PageRequest.of(0, 5)
        );
        return result;
    }

    /*
     * Get open product on market
     */
    @GetMapping("/open")
    public List<Product> getOpenProducts(
        @RequestBody GetOpenProductReqDto req
    ) {
        PageRequest pageReq = PageRequest.of(
            req.getPageNumber(), 
            req.getPageSize()
        );
        List<Product> products = productService.findAllOpenProducts(pageReq, req.getCategories());
        for (Product p : products) {
            p.setCategories(List.of());
            p.setBiddenPrices(List.of());
        }
        return products;
    }

    @PostMapping("/")
    public ResponseEntity<Product> addNewProduct(
        @RequestBody AddProductReqDto addProductDto
    ) throws IOException {
        Product product = productService.addNewProduct(addProductDto);
        return ResponseEntity.ok(product);
    }

    /*
     * Get bidding products of a bidder
     */
    @GetMapping("/bidding")
    public List<Product> getBiddingProduct(
        @RequestBody GetBiddingProductReqDto req
    ) {
        PageRequest pageRequest = PageRequest.of(
            req.getPageNumber(),
            req.getPageSize()
        );

        List<Product> result = productService.findBiddingProducts(
            req.getBidderId(),
            pageRequest
        );
        return result;
    }

    @GetMapping("/bought")
    public List<SoldProduct> getSoldProductByBidderId(
        @RequestBody GetSoldProductsByBidderReqDto req
    ) {
        List<SoldProduct> products = productService.findSoldProductByBidderId(
            req.getBidderId(), 
            PageRequest.of(req.getPageNumber(), req.getPageSize())
        );
        products.forEach(product -> product.setBiddenPrices(List.of()));
        return products;
    }

    @GetMapping("single-info")
    public Product getProductInfo(
        @RequestBody GetProductReqDto req
    ) {
        Product product = productService.findById(req.getProductId());
        return product;
    }

    @PutMapping("/")
    public String updateProductInfoByAuctioneer(
        @RequestBody UpdateProductRequestDto req
    ) {
        boolean success = productService.updateProductInfo(
            req.getAuctioneerId(),
            req.getProductId()
        );
        return String.valueOf(success);
    }

    @DeleteMapping("/")
    public String deleteProduct(
        @RequestBody DeleteProductReqDto req
    ) {
        boolean success = productService.deleteProduct(
            req.getAuctioneerId(),
            req.getProductId()
        );
        return String.valueOf(success);
    }

}
