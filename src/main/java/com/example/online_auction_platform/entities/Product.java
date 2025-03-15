package com.example.online_auction_platform.entities;

import java.time.LocalDateTime;
import java.util.Hashtable;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "product")
@Inheritance(strategy = InheritanceType.JOINED)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;

    @Column(name = "imageUrl")
    protected String imageUrl;

    @Column(name = "beginning_price")
    protected int beginningPrice;

    @Column(name = "current_price")
    protected int currentPrice;

    @Column(name = "state")
    protected String state;
    
    @Column(name = "location")
    protected String location;

    @Column(name = "name")
    protected String name;
    
    @Column(name = "created_date")
    protected LocalDateTime createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auctioneer_id")
    @JsonIgnore
    protected Auctioneer auctioneer;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    protected List<BiddenPrice> biddenPrices;

    @ManyToMany
    @JoinTable(
        name = "product_category",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    protected List<Category> categories;

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", imageUrl='" + imageUrl + '\'' +
                ", beginningPrice=" + beginningPrice +
                ", currentPrice=" + currentPrice +
                ", state=" + state +
                ", auctioneer=" + auctioneer.getId() +
                ", biddenPrices=" + biddenPrices.stream().map((e) -> {return e.getId();}).collect(Collectors.toList()) +
                '}';
    }
}
