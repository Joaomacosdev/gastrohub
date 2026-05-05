package br.com.gastrohub.infra.exception;

public class NotFoundException extends RuntimeException{

    public NotFoundException(String entity, Object id) {
        super(entity + " não encontrado com id: " + id);
    }
}
