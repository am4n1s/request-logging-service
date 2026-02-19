package com.nurzhigit.amanov.qazcodetask.controller;

import com.nurzhigit.amanov.qazcodetask.service.AsyncService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@RestController
public class TestController {
    private final AsyncService asyncService;

    public TestController(AsyncService asyncService) {
        this.asyncService = asyncService;
    }

    @GetMapping("/ok")
    public String ok() {
        return "OK";
    }
    @GetMapping("/slow")
    public String slow() throws InterruptedException {
        Thread.sleep(3000);
        return "Slow OK";
    }
    @GetMapping("/async-slow")
    public CompletableFuture<String> asyncSlow() {
       return asyncService.asyncSlow();
    }
    @GetMapping("/cpu-bound")
    public String cpuBound() {
        int[] arr = new Random().ints(1_000_000).toArray();
        Arrays.sort(arr);
        return "CPU OK";
    }
    @GetMapping("/error")
    public String error() {
        throw new RuntimeException("Something went wrong");
    }
}
