package pl.marcindev.demogittask.githubservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;
import pl.marcindev.demogittask.githubservice.model.Branch;
import pl.marcindev.demogittask.githubservice.model.Commit;
import pl.marcindev.demogittask.githubservice.model.MyRepository;
import pl.marcindev.demogittask.githubservice.model.RepoBranchCommit;
import pl.marcindev.demogittask.githubservice.service.exceptions.CustomNotAcceptableHeader;
import pl.marcindev.demogittask.githubservice.service.exceptions.CustomNotFoundUserException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GitHubService {
    private final WebClient webClient;
    private final String GITHUB_API_URL = "https://api.github.com";

    public Flux<RepoBranchCommit> getRepositories(String owner) {
        String uriRepoList = UriComponentsBuilder.fromUriString(GITHUB_API_URL)
                .pathSegment("users", owner, "repos")
                .build().toUriString();

        return webClient.get()
                .uri(uriRepoList)
                .header("Accept", "application/json")
                .retrieve()
                .bodyToFlux(MyRepository.class)
                .onErrorResume(throwable -> {
                    if (throwable instanceof WebClientResponseException) {
                        WebClientResponseException ex = (WebClientResponseException) throwable;
                        if (ex.getStatusCode().value() == 404) {
                            String message = ex.getMessage();
                            return Flux.error(new CustomNotFoundUserException(HttpStatus.NOT_FOUND.value(), message));
                        } else if (ex.getStatusCode().value() == 406) {
                            String message = ex.getMessage();
                            return Flux.error(new CustomNotAcceptableHeader(HttpStatus.NOT_ACCEPTABLE.value(), message));
                        }
                    }
                    return Flux.error(throwable);
                })
                .filter(repository -> !repository.fork())
                .flatMap(repository -> {
                    return webClient.get()
                            .uri(getRepositoryUri(owner, repository.name()))
                            .retrieve()
                            .bodyToFlux(Branch.class)
                            .concatMap(branch -> {
                                Mono<Commit> commitMono = webClient.get()
                                        .uri(getBranchesUri(owner, repository.name(), branch.name()))
                                        .retrieve()
                                        .bodyToMono(Commit.class);

                                return commitMono
                                        .map(commit -> new RepoBranchCommit(repository.name(),
                                                branch.name(), commit.sha()));
                            });
                });
    }

    private String getBranchesUri(String owner, String repoName, String branchName) {
        return UriComponentsBuilder.fromUriString(GITHUB_API_URL)
                .pathSegment("repos", owner, repoName, "branches", branchName)
                .build().toUriString();
    }

    private String getRepositoryUri(String owner, String repoName) {
        return UriComponentsBuilder.fromUriString(GITHUB_API_URL)
                .pathSegment("repos", owner, repoName)
                .build().toUriString();
    }
}
