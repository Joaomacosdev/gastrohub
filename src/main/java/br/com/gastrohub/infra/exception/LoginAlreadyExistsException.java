package br.com.gastrohub.infra.exception;

public class LoginAlreadyExistsException extends RuntimeException{
    public LoginAlreadyExistsException(String message) {
        super(message);
    }
}
