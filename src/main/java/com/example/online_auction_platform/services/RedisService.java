package com.example.online_auction_platform.services;

public interface RedisService {
    
    public Object getObject(Object key);

    public void cacheObject(Object key, Object value);

    

}
