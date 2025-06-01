package io.github.jotabrc.handler;

public class AuthenticationDeniedException extends RuntimeException {
    public AuthenticationDeniedException(String message) {
        super(message);
    }
}
