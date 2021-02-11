package com.learnjava.completablefuture;

import com.learnjava.service.HelloWorldService;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.learnjava.util.CommonUtil.delay;
import static com.learnjava.util.CommonUtil.startTimer;
import static com.learnjava.util.CommonUtil.timeTaken;
import static com.learnjava.util.LoggerUtil.log;

public class CompletableFutureHelloWorld {

    private HelloWorldService hws;

    private ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

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


    public String helloworld_3_async_calls_log_async() {
        startTimer();
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> hws.hello(), executorService);
        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> hws.world(), executorService);
        CompletableFuture<String> hiCompletableFuture = CompletableFuture.supplyAsync(() -> {
            delay(1000);
            return " Hi completablefuture!";
        }, executorService);

        String result = hello
                .thenCombineAsync(world, (h, w) -> {  //terminal async, vai jogar para outra thread
                    log("thenCombine h/w");
                    return h + w;
                }, executorService)
                .thenCombineAsync(hiCompletableFuture, (hw, hi) -> {  //terminal async, vai jogar para outra thread
                    log("thenCombine previous/current");
                    return hw + hi;
                }, executorService)
                .thenApplyAsync(s -> { //terminal async, vai jogar para outra thread
                    log("thenApply");
                    return s.toUpperCase();
                }, executorService)
                .join();
        timeTaken();

        return result;
    }
}
