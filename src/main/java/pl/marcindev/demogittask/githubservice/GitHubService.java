package pl.marcindev.demogittask.githubservice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class GitHubService {
    private final WebClient webClient;
    

}
