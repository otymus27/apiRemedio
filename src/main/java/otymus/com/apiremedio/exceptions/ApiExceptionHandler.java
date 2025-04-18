package otymus.com.apiremedio.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component // necessário para o Spring enxergar como bean e usar no SecurityConfig
@ControllerAdvice
public class ApiExceptionHandler implements AuthenticationEntryPoint {

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
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Map<String, String> handleBadCredentials(BadCredentialsException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("erro", "Credenciais inválidas");
        error.put("mensagem", "Usuário ou senha incorretos.");
        return error;
    }

    @ResponseBody
    @ExceptionHandler(AuthorizationDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Map<String, String> handleAuthorizationDenied(AuthorizationDeniedException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("erro", "Acesso negado, tem que ter perfil de Administrador!!!");
        error.put("mensagem", ex.getMessage()); // Ou uma mensagem fixa, se preferir
        return error;
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleGenericException(Exception ex) {
        Map<String, String> error = new HashMap<>();
        error.put("erro", "Erro interno no servidor");
        error.put("mensagem", "Ocorreu um erro inesperado. Por favor, tente novamente mais tarde.");
        // Log the exception for debugging purposes
        ex.printStackTrace();
        return error;
    }

    @ResponseBody
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("erro", "Endpoint não encontrado");
        error.put("mensagem", "O caminho '" + ex.getRequestURL() + "' não existe.");
        error.put("path", ex.getRequestURL());
        return error;
    }

    // Método para tratar quando o usuário não está autenticado
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         org.springframework.security.core.AuthenticationException authException) throws IOException {
        System.out.println("===> Método commence() chamado!");

        String path = request.getRequestURI();
        // Simula checagem de endpoint válido (ideal: fazer isso com mais controle)
        boolean endpointValido = path.matches("(/login|/usuarios|/outros-validos)(/.*)?");

        if (!endpointValido) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            new ObjectMapper().writeValue(response.getOutputStream(), Map.of(
                    "erro", "Endpoint não encontrado",
                    "mensagem", "O caminho '" + path + "' não existe.",
                    "path", path
            ));
            return;
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        new ObjectMapper().writeValue(response.getOutputStream(), Map.of(
                "erro", "Não autorizado",
                "mensagem", "Você precisa estar autenticado para acessar este recurso.",
                "path", path
        ));
    }
}