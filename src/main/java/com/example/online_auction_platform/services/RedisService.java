package com.example.online_auction_platform.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RedisService {
    
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public Object getObject(Object key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void cacheObject(Object key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void cacheList(String key, List<? extends Object> page) {
        try {
            String json = objectMapper.writeValueAsString(page);
            redisTemplate.opsForValue().set(key, json);
        } catch (Exception e) {
            System.out.println("Cache page error");
            e.printStackTrace();
        }
    }

    public List<? extends Object> getList(String key) {
        Object result = redisTemplate.opsForValue().get(key);
        if (result instanceof List) {
            return (List) result;
        } else {
            return List.of();
        }
        // List<? extends Object> data = (List) ;
        // if (data != null) {
        //     JavaType type = objectMapper.getTypeFactory().constructParametricType(PageImpl.class, Product.class);
        //     try {
        //         return objectMapper.readValue(json, type);
        //     } catch (Exception e) {
        //         System.out.println("Get page error");
        //         return List.of();
        //     }
        // }
        // System.out.println("No data for key");
        // return List.of();
    }
}