package com.learnjava;

import com.learnjava.completablefuture.CompletableFutureHelloWorld;
import com.learnjava.service.HelloWorldService;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

import static com.learnjava.util.CommonUtil.startTimer;
import static com.learnjava.util.CommonUtil.timeTaken;
import static com.learnjava.util.LoggerUtil.log;
import static org.junit.Assert.assertEquals;

public class CompletableFutureHelloWorldTest {

    HelloWorldService hws = new HelloWorldService();
    CompletableFutureHelloWorld completableFutureHelloWorld = new CompletableFutureHelloWorld(hws);

    @Test
    void helloWorld() {
        CompletableFuture<String> completableFuture = completableFutureHelloWorld.helloWorld();

        completableFuture.thenAccept(s -> {
            assertEquals("HELLO WORLD", s);
        }).join();
    }

    @Test
    void helloWorld_compose() {
        startTimer();
        CompletableFuture<String> completableFuture = completableFutureHelloWorld.helloWorld_thenCompose();

        completableFuture.thenAccept(s -> {
            assertEquals("HELLO WORLD!", s);
        }).join();
        timeTaken();
    }

    @Test
    void test_combine_completable() {
        String helloWorld = completableFutureHelloWorld.helloworld_multiple_async_calls();
        assertEquals("HELLO WORLD!", helloWorld);
    }

    @Test
    void test_combine_completable_3() {
        String helloWorld = completableFutureHelloWorld.helloworld_3_async_calls();
        log(helloWorld);
        assertEquals("HELLO WORLD! HI COMPLETABLEFUTURE!", helloWorld);
    }

    @Test
    void test_combine_completable_3_log() {
        String helloWorld = completableFutureHelloWorld.helloworld_3_async_calls_log_async();
        log(helloWorld);
        assertEquals("HELLO WORLD! HI COMPLETABLEFUTURE!", helloWorld);
    }
}
