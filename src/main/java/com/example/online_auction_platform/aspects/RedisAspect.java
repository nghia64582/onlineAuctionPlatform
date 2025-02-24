package com.example.online_auction_platform.aspects;

import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.example.online_auction_platform.entities.Product;
import com.example.online_auction_platform.services.RedisService;

@Aspect
@Component
public class RedisAspect {

    @Autowired
    RedisService redisService;
    
    // setup pointcut declarations
    @Pointcut("execution(* com.example.online_auction_platform.services.ProductService.findByAuctioneerId(..))")
    private void findByAuctioneerId() {}

    @Around("findByAuctioneerId()")
    public Object around(ProceedingJoinPoint proceedJoinPoint) {
        Object[] args = proceedJoinPoint.getArgs();
        int auctioneerId = (int) args[0];
        Pageable pageable = (Pageable) args[1];
        // key of object in redis
        String key = String.format(
            "Product_%d_%d_%d", 
            auctioneerId,
            pageable.getPageSize(),
            pageable.getOffset()
        );
        List<Product> redisCacheListProduct = (List<Product>) redisService.getList(key);
        // if the result is already in the cache, so return the cached value
        if (redisCacheListProduct != null && !redisCacheListProduct.isEmpty()) {
            return redisCacheListProduct;
        }
        try {
            List<? extends Object> result = (List<? extends Object>) proceedJoinPoint.proceed();
            redisService.cacheList(key, result);
            return result;
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }
}
