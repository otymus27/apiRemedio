package otymus.com.apiremedio.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException; // Importe AccessDeniedException
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ApiExceptionHandler {

    @ResponseBody
    @ExceptionHandler(RemedioNaoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleRemedioNaoEncontrado(RemedioNaoEncontradoException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("erro", "Recurso não encontrado");
        error.put("mensagem", ex.getMessage());
        return error;
    }

    @ResponseBody
    @ExceptionHandler(DadosInvalidosException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleDadosInvalidos(DadosInvalidosException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("erro", "Dados inválidos");
        error.put("mensagem", ex.getMessage());
        return error;
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return errors;
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleGenericException(Exception ex) {
        if (ex instanceof AccessDeniedException) {
            throw (AccessDeniedException) ex; // Re-lança para ser tratado pelo AccessDeniedHandler
        }
        Map<String, String> error = new HashMap<>();
        error.put("erro", "Erro interno no servidor");
        error.put("mensagem", "Ocorreu um erro inesperado. Por favor, tente novamente mais tarde.");
        ex.printStackTrace();
        return error;
    }
}