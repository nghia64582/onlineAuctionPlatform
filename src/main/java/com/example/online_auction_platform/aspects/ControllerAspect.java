package com.example.online_auction_platform.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Aspect
@Component
public class ControllerAspect {


    private HashMap<String, AtomicInteger> methodCounter;

    private ControllerAspect() {
        methodCounter = new HashMap<>();
    }

    // setup pointcut declarations
    @Pointcut("execution(* com.example.online_auction_platform.controllers.*.*(..))")
    private void forControllerPackage() {}

    // do the same for service and dao
    @Pointcut("execution(* com.example.online_auction_platform.services.*.*(..))")
    private void forServicePackage() {}

    @Pointcut("forControllerPackage() || forServicePackage()")
    private void forAppFlow() {}

    // add @Before advice
    @Before("forAppFlow()")
    public void counter(JoinPoint theJoinPoint) {
        // display method we are calling
        String theMethod = theJoinPoint.getSignature().toShortString();
        if (!methodCounter.containsKey(theMethod)) {
            methodCounter.put(theMethod, new AtomicInteger(0));
        }
        int count = methodCounter.get(theMethod).incrementAndGet();
        System.out.println("Method " + theMethod + " has been used " + count + " times.");
    }

    @Around("forAppFlow()")
    public Object around(ProceedingJoinPoint proceedJoinPoint) {
        // display method we are returning from
        long start = System.currentTimeMillis();
        Object result = null;

        try {
            result = proceedJoinPoint.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        String method = proceedJoinPoint.toShortString();
        long current = System.currentTimeMillis();
        System.out.println("Method " + method + " took " + (current - start) + " ms to run.");
        return result;
    }
}











