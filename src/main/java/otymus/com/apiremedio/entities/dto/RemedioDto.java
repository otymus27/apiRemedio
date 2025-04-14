package otymus.com.apiremedio.entities.dto;

import otymus.com.apiremedio.entities.enums.Laboratorio;
import otymus.com.apiremedio.entities.enums.Via;

public record RemedioDto(
        String nome,
        Via via,
        String lote,
        int quantidade,
        String validade,
        Laboratorio laboratorio
) {

}
