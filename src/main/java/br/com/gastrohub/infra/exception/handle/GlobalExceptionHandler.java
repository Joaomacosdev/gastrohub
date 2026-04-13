package br.com.gastrohub.infra.exception.handle;

import br.com.gastrohub.infra.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail handleNotFound(NotFoundException ex, HttpServletRequest request) {
      return   ProblemDetailFactory.create(
                HttpStatus.NOT_FOUND,
                "Usuário não encontrado",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ProblemDetail handleEmailAlreadyExists(EmailAlreadyExistsException ex, HttpServletRequest request) {
        return ProblemDetailFactory.create(
                HttpStatus.CONFLICT,
                "Email já cadastrado",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(LoginAlreadyExistsException.class)
    public ProblemDetail handleEmailLoginAlreadyExists(LoginAlreadyExistsException ex, HttpServletRequest request) {
        return ProblemDetailFactory.create(
                HttpStatus.CONFLICT,
                "Login já cadastrado",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(ValidationException.class)
    public ProblemDetail handleValidation(ValidationException ex, HttpServletRequest request) {

        return ProblemDetailFactory.create(
                HttpStatus.BAD_REQUEST,
                "Erro de validação",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(BusinessException.class)
    public ProblemDetail handleBusiness(BusinessException ex, HttpServletRequest request) {

        return ProblemDetailFactory.create(
                HttpStatus.UNPROCESSABLE_ENTITY,
                "Erro de regra de negócio",
                ex.getMessage(),
                request.getRequestURI()
        );
    }


    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(Exception ex, HttpServletRequest request) {
        return ProblemDetailFactory.create(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro interno",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

}
