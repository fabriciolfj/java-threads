package com.learnjava.apiclient;

import com.learnjava.domain.github.GitHubPosition;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.learnjava.util.CommonUtil.startTimer;
import static com.learnjava.util.CommonUtil.timeTaken;
import static com.learnjava.util.LoggerUtil.log;

public class GitHubJobsClient {

    private WebClient webClient;

    public GitHubJobsClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public List<GitHubPosition> invokeJobsApi(int pageNum, String description) {
        var uri = UriComponentsBuilder.fromUriString("/positions.json")
                .queryParam("description", description)
                .queryParam("page", pageNum)
                .buildAndExpand()
                .toUriString();

        log("uri: " + uri);

        var github = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToFlux(GitHubPosition.class)
                .collectList()
                .block();

        return github;
    }

    public List<GitHubPosition> invokeMultiplesPages(List<Integer> pageNumbers, String description) {
        startTimer();
        List<GitHubPosition> gitHubPositions = pageNumbers.stream()
                .map(pageNumber -> invokeJobsApi(pageNumber, description))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        timeTaken();
        return gitHubPositions;
    }

    public List<GitHubPosition> invokeMultiplesPagesCf(List<Integer> pageNumbers, String description) {
        startTimer();
        var result = pageNumbers.stream()
                .map(pageNumber -> CompletableFuture.supplyAsync(() -> invokeJobsApi(pageNumber, description)))
                .collect(Collectors.toList());

        List<GitHubPosition> gitHubPositions =
                result.stream()
                .map(CompletableFuture::join)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        timeTaken();
        return gitHubPositions;
    }

    public List<GitHubPosition> invokeMultiplesPagesCfLisfOff(List<Integer> pageNumbers, String description) {
        startTimer();
        var result = pageNumbers.stream()
                .map(pageNumber -> CompletableFuture.supplyAsync(() -> invokeJobsApi(pageNumber, description)))
                .collect(Collectors.toList());

        CompletableFuture<Void> cfAllOf = CompletableFuture.allOf(result.toArray(new CompletableFuture[result.size()]));


        List<GitHubPosition> gitHubPositions =
                cfAllOf.thenApply(v -> result.stream()
                        .map(CompletableFuture::join)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList()))
                .join();

        timeTaken();
        return gitHubPositions;
    }
}
