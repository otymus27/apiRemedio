package otymus.com.apiremedio.entities.dto;


import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

@Validated
public record UsuarioCadastrarDto(
        @NotBlank //validação para campo vazio
        String login,
        @NotBlank //validação para campo vazio
        String senha) {
}
