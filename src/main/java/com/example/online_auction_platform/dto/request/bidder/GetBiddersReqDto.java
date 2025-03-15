package com.example.online_auction_platform.dto.request.bidder;

import lombok.Data;

@Data
public class GetBiddersReqDto {
    int pageNumber;
    int pageSize;
}
