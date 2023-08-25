package pl.marcindev.demogittask.githubservice.model;

public record MyRepository(String fullName,boolean fork,Owner owner, Branch branch) {
}
