package otymus.com.apiremedio.entities;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.EqualsAndHashCode;
import otymus.com.apiremedio.entities.dto.RemedioAtualizarDto;
import otymus.com.apiremedio.entities.dto.RemedioCadastrarDto;
import otymus.com.apiremedio.entities.enums.Laboratorio;
import otymus.com.apiremedio.entities.enums.Via;

import java.time.LocalDate;

@Entity(name="Remedio")
@Table(name = "tb_remedios")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Remedio {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;

    @Enumerated(EnumType.STRING)
    private Via via;
    private String lote;
    private int quantidade;
    private LocalDate validade;

    @Enumerated(EnumType.STRING)
    private Laboratorio laboratorio;

    //Construtor personalizado para enviar dados para dto
    public Remedio(RemedioCadastrarDto dados){
        this.nome=dados.nome();
        this.via = dados.via();
        this.lote = dados.lote();
        this.quantidade = dados.quantidade();
        this.validade = dados.validade();
        this.laboratorio = dados.laboratorio();
    }

    //construtor padrão vazio
    public Remedio() {
    }

    //Método para atualizar os dados
    public void atualizarDados(@Valid RemedioAtualizarDto dados) {
        if(dados.nome() !=null) {
            this.nome = dados.nome();
        }

        if(dados.via() !=null) {
            this.via = dados.via();
        }

        if(dados.laboratorio() !=null) {
            this.laboratorio = dados.laboratorio();
        }
    }
}
