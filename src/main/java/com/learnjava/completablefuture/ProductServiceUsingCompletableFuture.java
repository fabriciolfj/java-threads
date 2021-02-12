package com.learnjava.completablefuture;

import com.learnjava.domain.Inventory;
import com.learnjava.domain.Product;
import com.learnjava.domain.ProductInfo;
import com.learnjava.domain.ProductOption;
import com.learnjava.domain.Review;
import com.learnjava.service.InventoryService;
import com.learnjava.service.ProductInfoService;
import com.learnjava.service.ReviewService;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static com.learnjava.util.CommonUtil.delay;
import static com.learnjava.util.CommonUtil.stopWatch;
import static com.learnjava.util.CommonUtil.timeTaken;
import static com.learnjava.util.LoggerUtil.log;

public class ProductServiceUsingCompletableFuture {
    private ProductInfoService productInfoService;
    private ReviewService reviewService;
    private InventoryService inventoryService;

    public ProductServiceUsingCompletableFuture(ProductInfoService productInfoService, ReviewService reviewService) {
        this.productInfoService = productInfoService;
        this.reviewService = reviewService;
    }

    public ProductServiceUsingCompletableFuture(ProductInfoService productInfoService, ReviewService reviewService, InventoryService inventoryService) {
        this.productInfoService = productInfoService;
        this.reviewService = reviewService;
        this.inventoryService = inventoryService;
    }

    public Product retrieveProductDetails(String productId) throws InterruptedException {
        stopWatch.start();

        CompletableFuture<ProductInfo> productInfo = CompletableFuture.supplyAsync(() -> productInfoService.retrieveProductInfo(productId));
        CompletableFuture<Review> review = CompletableFuture.supplyAsync(() -> reviewService.retrieveReviews(productId));
        CompletableFuture<Product> product = productInfo.thenCombine(review, (p, r) -> new Product(productId, p, r));

        stopWatch.stop();
        log("Total Time Taken : "+ stopWatch.getTime());
        return product.join();
    }

    public Product retrieveProductDetailsWithInventory(String productId) throws InterruptedException {
        stopWatch.start();

        CompletableFuture<ProductInfo> productInfo =
                CompletableFuture.supplyAsync(() -> productInfoService.retrieveProductInfo(productId))
                .thenApply(productInfo1 -> {
                    productInfo1.setProductOptions(updateInventario2(productInfo1));
                    return productInfo1;
                });


        CompletableFuture<Review> review = CompletableFuture
                .supplyAsync(() -> reviewService.retrieveReviews(productId))
                .exceptionally(e -> {
                    log("Handle the exception in reviewservice:" + e.getMessage());
                    return Review
                            .builder()
                            .noOfReviews(0)
                            .overallRating(0)
                            .build();
                });

        CompletableFuture<Product> product = productInfo.thenCombine(review, (p, r) -> new Product(productId, p, r))
                .whenComplete((p, ex) -> {
                    log("Inside whenComplete: " + p + " and the exception is " + ex);
                });

        timeTaken();
        return product.join();
    }

    private List<ProductOption> updateInventario(ProductInfo productInfo) {
        List<ProductOption> options = productInfo.getProductOptions()
                .stream()
                .map(productOption -> {
                    final Inventory inventory = inventoryService.addInventory(productOption);
                    productOption.setInventory(inventory);
                    return productOption;
                }).collect(Collectors.toList());

        return options;
    }

    private List<ProductOption> updateInventario2(ProductInfo productInfo) {
        List<CompletableFuture<ProductOption>>options = productInfo.getProductOptions()
                .stream()
                .map(productOption -> {
                    return CompletableFuture.supplyAsync(() -> inventoryService.addInventory(productOption))
                            .thenApply(inventory -> {
                                productOption.setInventory(inventory);
                                return productOption;
                            });
                }).collect(Collectors.toList());

        return options.stream().map(CompletableFuture::join).collect(Collectors.toList());
    }

    public ProductInfo getProduto() throws ExecutionException, InterruptedException {
        return CompletableFuture.supplyAsync(() -> productInfoService.retrieveProductInfo("ABC"))
                .exceptionally(e -> {
                    log("Fail, details: " + e.getMessage());
                    return ProductInfo.builder()
                            .productId("0")
                            .productOptions(Collections.EMPTY_LIST)
                            .build();
                }).whenComplete((p, e) -> {
                    log("Product: " + p);
                }).get();
    }

    public static void main(String[] args) throws InterruptedException {

        ProductInfoService productInfoService = new ProductInfoService();
        ReviewService reviewService = new ReviewService();
        ProductServiceUsingCompletableFuture productService = new ProductServiceUsingCompletableFuture(productInfoService, reviewService);
        String productId = "ABC123";
        Product product = productService.retrieveProductDetails(productId);
        log("Product is " + product);
        delay(1000);

    }
}
