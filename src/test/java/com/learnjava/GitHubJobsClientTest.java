package com.learnjava;

import com.learnjava.apiclient.GitHubJobsClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class GitHubJobsClientTest {

    private WebClient webClient = WebClient.create("https://jobs.github.com/");
    GitHubJobsClient gitHubJobsClient = new GitHubJobsClient(webClient);

    @Test
    void invokeGithubPageNumber() {
        int pageNum = 1;
        String description = "JavaScript";

        var result = gitHubJobsClient.invokeJobsApi(pageNum, description);

        assertTrue(result.size() > 0);
        result.forEach(Assertions::assertNotNull);
    }

    @Test
    void invokeGithubPageNumberMultiplePages() {
        List<Integer> pages = List.of(1,2,3);
        String description = "JavaScript";

        var result = gitHubJobsClient.invokeMultiplesPages(pages, description);

        assertTrue(result.size() > 0);
        result.forEach(Assertions::assertNotNull);
    }

    @Test
    void invokeGithubPageNumberMultiplePagescf() {
        List<Integer> pages = List.of(1,2,3);
        String description = "JavaScript";

        var result = gitHubJobsClient.invokeMultiplesPagesCf(pages, description);

        assertTrue(result.size() > 0);
        result.forEach(Assertions::assertNotNull);
    }

    @Test
    void invokeGithubPageNumberMultiplePagescfAllOf() {
        List<Integer> pages = List.of(1,2,3);
        String description = "JavaScript";

        var result = gitHubJobsClient.invokeMultiplesPagesCfLisfOff(pages, description);

        assertTrue(result.size() > 0);
        result.forEach(Assertions::assertNotNull);
    }
}
