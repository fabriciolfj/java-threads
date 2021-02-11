package com.learnjava.completablefuture;

import com.learnjava.service.HelloWorldService;

import java.util.concurrent.CompletableFuture;

import static com.learnjava.util.CommonUtil.delay;
import static com.learnjava.util.CommonUtil.startTimer;
import static com.learnjava.util.CommonUtil.timeTaken;
import static com.learnjava.util.LoggerUtil.log;

public class CompletableFutureHelloWorld {

    private HelloWorldService hws;

    public CompletableFutureHelloWorld(HelloWorldService hws) {
        this.hws = hws;
    }

    public CompletableFuture<String> helloWorld_thenCompose() {
        return CompletableFuture.supplyAsync(hws::hello)
                .thenCompose(p -> hws.worldFuture(p))
                .thenApply(String::toUpperCase);
    }

    public CompletableFuture<String> helloWorld() {
        return CompletableFuture.supplyAsync(hws::helloWorld)
                .thenApply(String::toUpperCase);
    }

    public String helloworld_3_async_calls() {
        startTimer();
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(hws::hello);
        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> hws.world());
        CompletableFuture<String> hiCompletableFuture = CompletableFuture.supplyAsync(() -> {
            delay(1000);
            return " Hi completablefuture!";
        });

        String result = hello.thenCombine(world, (h,w) -> h + w)
                .thenCombine(hiCompletableFuture, (hw, hi) -> hw + hi)
                .thenApply(String::toUpperCase)
                .join();
        timeTaken();

        return result;
    }


    public String helloworld_multiple_async_calls() {
        startTimer();
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(hws::hello);
        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> hws.world());

        String result = hello.thenCombine(world, (h,w) -> h + w)
                .thenApply(String::toUpperCase)
                .join();
        timeTaken();

        return result;
    }

    public static void main(String[] args) {
        HelloWorldService hws = new HelloWorldService();
        CompletableFuture.supplyAsync(hws::helloWorld)
                .thenApply(String::toUpperCase)
                .thenAccept(result -> {
                    log("Result is: " + result);
                }).join();
        log("Done!");
        ;;delay(2000);
    }
}
