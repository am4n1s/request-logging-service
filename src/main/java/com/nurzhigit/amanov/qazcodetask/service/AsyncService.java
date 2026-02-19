package com.nurzhigit.amanov.qazcodetask.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class AsyncService {
    @Async("logExecutor")
    public CompletableFuture<String> asyncSlow() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {}

        return CompletableFuture.completedFuture("Async Slow OK");
    }
}
