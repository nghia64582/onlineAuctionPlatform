package com.example.online_auction_platform.entities;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sold_product")
public class SoldProduct extends Product {
    
    @PrimaryKeyJoinColumn(name = "id")

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bidder_id")
    private Bidder bidder;
    
    @Column(name = "sold_date")
    private LocalDateTime soldDate;

}
