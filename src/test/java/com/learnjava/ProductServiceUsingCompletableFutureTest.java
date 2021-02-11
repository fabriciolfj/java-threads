package com.learnjava;

import com.learnjava.completablefuture.ProductServiceUsingCompletableFuture;
import com.learnjava.domain.Product;
import com.learnjava.service.InventoryService;
import com.learnjava.service.ProductInfoService;
import com.learnjava.service.ReviewService;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ProductServiceUsingCompletableFutureTest {

    ProductInfoService productInfoService = new ProductInfoService();
    ReviewService reviewService = new ReviewService();
    InventoryService inventoryService = new InventoryService();
    ProductServiceUsingCompletableFuture productService = new ProductServiceUsingCompletableFuture(productInfoService, reviewService, inventoryService);

    @Test
    void retrieveProductDetails() throws InterruptedException {
        Product product = productService.retrieveProductDetailsWithInventory("ABCD");
        assertNotNull(product.getReview());
        assertNotNull(product.getProductInfo());
        assertTrue(product.getProductInfo().getProductOptions().size() > 0);
        product.getProductInfo().getProductOptions()
                .stream().forEach(c -> {
                    assertNotNull(c.getInventory());
        });
    }

}
