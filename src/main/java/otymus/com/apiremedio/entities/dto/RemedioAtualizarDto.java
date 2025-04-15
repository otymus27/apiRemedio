package otymus.com.apiremedio.entities.dto;

import jakarta.validation.constraints.NotNull;
import otymus.com.apiremedio.entities.enums.Laboratorio;
import otymus.com.apiremedio.entities.enums.Via;

public record RemedioAtualizarDto(
        @NotNull
        Long id,
        String nome,
        Via via,
        Laboratorio laboratorio
) {
}
