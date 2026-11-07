package com.example.requestlog.service;

import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Profile("dev")
@Service
public class AsyncService {

    @Async("asyncExecutor")
    public CompletableFuture<String> asyncSlow() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return CompletableFuture.failedFuture(e);
        }
        return CompletableFuture.completedFuture("Async Slow OK");
    }
}
