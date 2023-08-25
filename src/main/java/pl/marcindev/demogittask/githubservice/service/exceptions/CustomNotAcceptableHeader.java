package pl.marcindev.demogittask.githubservice.service.exceptions;

public class CustomNotAcceptableHeader extends RuntimeException{
    private final int value;
    private final String message;

    public CustomNotAcceptableHeader(int value, String message) {
        this.value=value;
        this.message=message;
    }
}
