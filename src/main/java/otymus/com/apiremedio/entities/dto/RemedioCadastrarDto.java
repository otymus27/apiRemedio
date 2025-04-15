package otymus.com.apiremedio.entities.dto;

import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import otymus.com.apiremedio.entities.enums.Laboratorio;
import otymus.com.apiremedio.entities.enums.Via;

import java.time.LocalDate;

@Validated
public record RemedioCadastrarDto(

        @NotBlank //validação para campo vazio
        String nome,
        @Enumerated
        Via via,
        @NotBlank
        String lote,

        int quantidade,
        @Future //Valida pra não receber datas passadas
        LocalDate validade,
        @Enumerated
        Laboratorio laboratorio
) {

}
