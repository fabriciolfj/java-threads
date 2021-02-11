package com.learnjava.completablefuture;

import com.learnjava.service.HelloWorldService;

import java.util.concurrent.CompletableFuture;

import static com.learnjava.util.CommonUtil.delay;
import static com.learnjava.util.CommonUtil.startTimer;
import static com.learnjava.util.CommonUtil.timeTaken;
import static com.learnjava.util.LoggerUtil.log;

public class CompletableFutureHelloWorldException {

    /*
    * whencomplete -> recebe uma biconsumer, não existe fallback, tem que verificar se a excecao existe
    * handle -> recebe um bifunction, tem que verificar se consta a exceção, e existe fallback
    * exceptionally -> recebe uma excecao (não precisa verificar se existe, pois é acionada caso realmente ocorra) e possui fallback.
    * */

    private HelloWorldService hws;

    public CompletableFutureHelloWorldException(HelloWorldService hws) {
        this.hws = hws;
    }

    public String helloworld_3_async_calls() {
        startTimer();
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(hws::hello);
        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> hws.world());
        CompletableFuture<String> hiCompletableFuture = CompletableFuture.supplyAsync(() -> {
            delay(1000);
            return " Hi completablefuture!";
        });

        String result = hello
                .handle((res, e) -> {
                    log("res is: " + res);

                    if (e != null) {
                        log("Exception world is: " + e.getMessage());
                        return "";
                    }

                    return res;
                })
                .thenCombine(world, (h, w) -> h + w)
                .handle((res, e) -> {
                    log("res is: " + res);

                    if (e != null) {
                        log("Exception world is: " + e.getMessage());
                        return "";
                    }

                    return res;
                })
                .thenCombine(hiCompletableFuture, (hw, hi) -> hw + hi)
                .thenApply(String::toUpperCase)
                .join();
        timeTaken();

        return result;
    }

    public String helloworld_3_async_calls_exceptionally() {
        startTimer();
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(hws::hello);
        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> hws.world());
        CompletableFuture<String> hiCompletableFuture = CompletableFuture.supplyAsync(() -> {
            delay(1000);
            return " Hi completablefuture!";
        });

        String result = hello
                .exceptionally(e -> {
                    log("Exception world is: " + e.getMessage());
                    return "";
                })
                .thenCombine(world, (h, w) -> h + w)
                .exceptionally(e -> {
                    log("Exception world is: " + e.getMessage());
                    return "";
                })
                .thenCombine(hiCompletableFuture, (hw, hi) -> hw + hi)
                .thenApply(String::toUpperCase)
                .join();
        timeTaken();

        return result;
    }

    public String helloworld_3_whencomplete() {
        startTimer();
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(hws::hello);
        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> hws.world());
        CompletableFuture<String> hiCompletableFuture = CompletableFuture.supplyAsync(() -> {
            delay(1000);
            return " Hi completablefuture!";
        });

        String result = hello
                .whenComplete((res, e) -> {
                    log("res is: " + res);

                    if (e != null) {
                        log("Exception world is: " + e.getMessage());
                        throw new RuntimeException(e.getMessage());
                    }

                })
                .thenCombine(world, (h, w) -> h + w)
                .whenComplete((res, e) -> {
                    log("res is: " + res);

                    if (e != null) {
                        log("Exception world is: " + e.getMessage());
                    }

                })
                .thenCombine(hiCompletableFuture, (hw, hi) -> hw + hi)
                .thenApply(String::toUpperCase)
                .join();
        timeTaken();

        return result;
    }

    public String helloworld_4_whencomplete() {
        startTimer();
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(hws::hello);
        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> hws.world());
        CompletableFuture<String> hiCompletableFuture = CompletableFuture.supplyAsync(() -> {
            delay(1000);
            return " Hi completablefuture!";
        });

        String result = hello
                .whenComplete((res, e) -> {
                    log("res is: " + res);

                    if (e != null) {
                        log("Exception hello is: " + e.getMessage());
                        throw new RuntimeException(e.getMessage());
                    }

                })
                .thenCombine(world, (h, w) -> h + w)
                .whenComplete((res, e) -> {
                    log("res is: " + res);

                    if (e != null) {
                        log("Exception world is: " + e.getMessage());
                    }

                })
                .exceptionally(e -> { //pega as exceções lancadas nos 2 eventos acima e lança um fallback para ambos
                    log("Exception after combine");
                    return "";
                })
                .thenCombine(hiCompletableFuture, (hw, hi) -> hw + hi)
                .thenApply(String::toUpperCase)
                .join();
        timeTaken();

        return result;
    }
}
