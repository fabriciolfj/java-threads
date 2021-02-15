package com.learnjava;

import com.learnjava.completablefuture.CompletableFutureHelloWorld;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertTrue;

public class CompletableFutureAnyTest {

    private CompletableFutureHelloWorld helloWorld = new CompletableFutureHelloWorld();

    @Test
    void testAnyof() {
        String result = helloWorld.completableFutureAnyOf();

        assertTrue(result != null);
    }
}
