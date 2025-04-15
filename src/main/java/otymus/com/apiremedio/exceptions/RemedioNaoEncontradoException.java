package otymus.com.apiremedio.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RemedioNaoEncontradoException extends RuntimeException {

    public RemedioNaoEncontradoException(Long id) {
        super("Remédio com ID " + id + " não encontrado.");
    }

    public RemedioNaoEncontradoException(String nome) {
        super("Nenhum remédio encontrado com o nome: " + nome);
    }
}
