package com.learnjava;

import com.learnjava.completablefuture.CompletableFutureHelloWorldException;
import com.learnjava.service.HelloWorldService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CompletableFutureHelloWorldExceptionTest {

    @Mock
    HelloWorldService helloWorldService;

    @InjectMocks
    CompletableFutureHelloWorldException completableFutureHelloWorldException;

    @Test
    void cenario1() {
        when(helloWorldService.hello()).thenThrow(new RuntimeException("Exception hello mock"));
        when(helloWorldService.world()).thenCallRealMethod();

        String result = completableFutureHelloWorldException.helloworld_3_async_calls();

        assertEquals(" WORLD! HI COMPLETABLEFUTURE!", result);
    }

    @Test
    void cenario2() {
        when(helloWorldService.hello()).thenThrow(new RuntimeException("Exception hello mock"));
        when(helloWorldService.world()).thenThrow(new RuntimeException("Exception hello mock"));

        String result = completableFutureHelloWorldException.helloworld_3_async_calls();

        assertEquals(" HI COMPLETABLEFUTURE!", result);
    }

    @Test
    void cenario3() {
        when(helloWorldService.hello()).thenCallRealMethod();
        when(helloWorldService.world()).thenCallRealMethod();

        String result = completableFutureHelloWorldException.helloworld_3_async_calls();

        assertEquals("HELLO WORLD! HI COMPLETABLEFUTURE!", result);
    }

    @Test
    void cenario4() {
        when(helloWorldService.hello()).thenThrow(new RuntimeException("Exception hello mock"));
        when(helloWorldService.world()).thenCallRealMethod();

        String result = completableFutureHelloWorldException.helloworld_3_async_calls_exceptionally();

        assertEquals(" WORLD! HI COMPLETABLEFUTURE!", result);
    }

    @Test
    void cenario5() {
        when(helloWorldService.hello()).thenThrow(new RuntimeException("Exception hello mock"));
        when(helloWorldService.world()).thenThrow(new RuntimeException("Exception hello mock"));

        String result = completableFutureHelloWorldException.helloworld_3_async_calls_exceptionally();

        assertEquals(" HI COMPLETABLEFUTURE!", result);
    }

    @Test
    void cenario6() {
        when(helloWorldService.hello()).thenCallRealMethod();
        when(helloWorldService.world()).thenCallRealMethod();

        String result = completableFutureHelloWorldException.helloworld_3_async_calls_exceptionally();

        assertEquals("HELLO WORLD! HI COMPLETABLEFUTURE!", result);
    }

    @Test
    void cenario7() {
        when(helloWorldService.hello()).thenThrow(new RuntimeException("Exception hello mock"));
        when(helloWorldService.world()).thenCallRealMethod();

        assertThrows(RuntimeException.class, () -> completableFutureHelloWorldException.helloworld_3_whencomplete());
    }

    @Test
    void cenario8() {
        when(helloWorldService.hello()).thenThrow(new RuntimeException("Exception hello"));
        when(helloWorldService.world()).thenThrow(new RuntimeException("Exception hello mock"));

        String result = completableFutureHelloWorldException.helloworld_4_whencomplete();

        assertEquals(" HI COMPLETABLEFUTURE!", result);
    }
}
