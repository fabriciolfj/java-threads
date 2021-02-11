package com.learnjava;

import com.learnjava.completablefuture.ProductServiceUsingCompletableFuture;
import com.learnjava.domain.Product;
import com.learnjava.service.InventoryService;
import com.learnjava.service.ProductInfoService;
import com.learnjava.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceUsingCompletableFutureExceptionTest {

    @Mock
    private ProductInfoService productInfoService;
    @Mock
    private ReviewService reviewService;
    @Mock
    private InventoryService inventoryService;
    @InjectMocks
    private ProductServiceUsingCompletableFuture productServiceUsingCompletableFuture;

    @Test
    void retrieveProductDetailsWithInventoryReviewServiceExceptionTest() throws InterruptedException {
        final String productId = "ABC";
        when(productInfoService.retrieveProductInfo(any())).thenCallRealMethod();
        when(reviewService.retrieveReviews(any())).thenThrow(new RuntimeException("Fail request review"));
        when(inventoryService.addInventory(any())).thenCallRealMethod();

        final Product product = productServiceUsingCompletableFuture.retrieveProductDetailsWithInventory(productId);

        assertNotNull(product.getReview());
        assertTrue(product.getReview().getNoOfReviews() == 0);
        assertTrue(product.getReview().getOverallRating() == 0);
    }

    @Test
    void retrieveProductDetailsWithInventoryProductServiceExceptionTest() throws InterruptedException {
        final String productId = "ABC";
        when(productInfoService.retrieveProductInfo(any())).thenThrow(new RuntimeException("Fail request review"));
        when(reviewService.retrieveReviews(any())).thenCallRealMethod();

        assertThrows(RuntimeException.class, () -> productServiceUsingCompletableFuture.retrieveProductDetailsWithInventory(productId));
    }


}
