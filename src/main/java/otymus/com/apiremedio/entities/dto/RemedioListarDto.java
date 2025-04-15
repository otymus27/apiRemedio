package otymus.com.apiremedio.entities.dto;

import otymus.com.apiremedio.entities.Remedio;
import otymus.com.apiremedio.entities.enums.Laboratorio;
import otymus.com.apiremedio.entities.enums.Via;

import java.time.LocalDate;

public record RemedioListarDto(
        String nome,
        Via via,
        String lote,
        Laboratorio laboratorio,
        LocalDate validade
) {
    public RemedioListarDto(Remedio remedio) {
        this(remedio.getNome(), remedio.getVia(), remedio.getLote(), remedio.getLaboratorio(), remedio.getValidade());
    }
}
