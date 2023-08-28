package pl.marcindev.demogittask.githubservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.ObjectArraySerializer;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import pl.marcindev.demogittask.githubservice.model.Branch;
import pl.marcindev.demogittask.githubservice.model.Commit;
import pl.marcindev.demogittask.githubservice.model.MyRepository;
import pl.marcindev.demogittask.githubservice.model.Owner;
import reactor.core.publisher.Flux;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GitHubServiceTest {
    private MockWebServer mockWebServer;
    private GitHubService gitHubService;

    @BeforeEach
    void setupMOckWebServer(){
        mockWebServer = new MockWebServer();
        String mockUrl = mockWebServer.url("/").toString();
        WebClient webClient = WebClient.builder().
                baseUrl(mockUrl)
                .build();
        gitHubService = new GitHubService(webClient);
    }
    @AfterEach
    void shutdownMockWebServer() throws IOException{
        mockWebServer.shutdown();
    }

    @Test
    void getRepositories() throws Exception {
        //given


    }
}