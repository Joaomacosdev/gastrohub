package br.com.gastrohub.infra.exception.handle;

import br.com.gastrohub.infra.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;


@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail handleNotFound(NotFoundException ex, HttpServletRequest request) {
      return   ProblemDetailFactory.create(
              HttpStatus.NOT_FOUND,
              "NOT_FOUND",
              "Recurso não encontrado",
              ex.getMessage(),
              request.getRequestURI()
        );
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ProblemDetail handleEmailAlreadyExists(EmailAlreadyExistsException ex, HttpServletRequest request) {
        return ProblemDetailFactory.create(
                HttpStatus.CONFLICT,
                "EMAIL_ALREADY_EXISTS",
                "Email já cadastrado",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(LoginAlreadyExistsException.class)
    public ProblemDetail handleEmailLoginAlreadyExists(LoginAlreadyExistsException ex, HttpServletRequest request) {
        return ProblemDetailFactory.create(
                HttpStatus.CONFLICT,
                "LOGIN_ALREADY_EXISTS",
                "Login já cadastrado",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(ValidationException.class)
    public ProblemDetail handleValidation(ValidationException ex, HttpServletRequest request) {

        return ProblemDetailFactory.create(
                HttpStatus.BAD_REQUEST,
                "VALIDATION_ERROR",
                "Erro de validação",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String detail = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        return ProblemDetailFactory.create(
                HttpStatus.BAD_REQUEST,
                "VALIDATION_ERROR",
                "Erro de validação",
                detail.isBlank() ? "Dados de entrada inválidos" : detail,
                request.getRequestURI()
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        String detail = ex.getConstraintViolations()
                .stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining("; "));

        return ProblemDetailFactory.create(
                HttpStatus.BAD_REQUEST,
                "VALIDATION_ERROR",
                "Erro de validação",
                detail.isBlank() ? "Dados de entrada inválidos" : detail,
                request.getRequestURI()
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request) {
        return ProblemDetailFactory.create(
                HttpStatus.BAD_REQUEST,
                "VALIDATION_ERROR",
                "Erro de validação",
                "Corpo da requisição inválido ou mal formatado",
                request.getRequestURI()
        );
    }

    @ExceptionHandler(BusinessException.class)
    public ProblemDetail handleBusiness(BusinessException ex, HttpServletRequest request) {

        return ProblemDetailFactory.create(
                HttpStatus.UNPROCESSABLE_ENTITY,
                "BUSINESS_RULE_VIOLATION",
                "Erro de regra de negócio",
                ex.getMessage(),
                request.getRequestURI()
        );
    }


    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(Exception ex, HttpServletRequest request) {
        return ProblemDetailFactory.create(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "INTERNAL_SERVER_ERROR",
                "Erro inesperado. Tente novamente mais tarde.",
                "Ocorreu um erro inesperado. Tente novamente mais tarde.",
                request.getRequestURI()
        );
    }

}
