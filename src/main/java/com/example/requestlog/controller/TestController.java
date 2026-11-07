package com.example.requestlog.controller;

import com.example.requestlog.service.AsyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

/** Демо-эндпоинты для проверки логирования. Доступны только в профиле dev. */
@Profile("dev")
@RestController
@RequiredArgsConstructor
public class TestController {

    private final AsyncService asyncService;

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

    // не /error: этот путь занят BasicErrorController
    @GetMapping("/fail")
    public String fail() {
        throw new RuntimeException("Something went wrong");
    }
}
