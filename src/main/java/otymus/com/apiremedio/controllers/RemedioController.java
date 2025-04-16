package otymus.com.apiremedio.controllers;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import otymus.com.apiremedio.entities.Remedio;
import otymus.com.apiremedio.entities.dto.RemedioAtualizarDto;
import otymus.com.apiremedio.entities.dto.RemedioListarDto;
import otymus.com.apiremedio.entities.dto.RemedioCadastrarDto;
import otymus.com.apiremedio.repositories.RemedioRepository;
import otymus.com.apiremedio.services.RemedioService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/remedios") // Padroniza o caminho base da sua API
public class RemedioController {

    private static final Logger logger = LoggerFactory.getLogger(RemedioController.class);
    @Autowired
    private RemedioRepository remedioRepository;

    private RemedioService remedioService;
    public RemedioController(RemedioService remedioService) {
        this.remedioService = remedioService;
    }

    @PostMapping
    @Transactional // Garante a atomicidade da operação no banco de dados
    public ResponseEntity<Remedio> cadastrar(@RequestBody @Valid RemedioCadastrarDto dados, UriComponentsBuilder uriBuilder) {
        System.out.println(dados);
        Remedio remedioCadastrado = remedioService.cadastrar(new Remedio(dados));
        URI uri = uriBuilder.path("/remedios/{id}").buildAndExpand(remedioCadastrado.getId()).toUri();
        return ResponseEntity.created(uri).body(remedioCadastrado);
    }

    @GetMapping
    public ResponseEntity<List<Remedio>> listar() {
        List<Remedio> remedios = remedioService.listar();
        if (remedios.isEmpty()) {
            return ResponseEntity.noContent().build(); // Retorna 204 No Content se não houver remédios
        } else {
            return ResponseEntity.ok(remedios); // Retorna 200 OK com a lista de remédios no corpo
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Remedio> buscarPorId(@PathVariable Long id) {
        return remedioService.buscarPorId(id)
                .map(ResponseEntity::ok) // Se o remédio for encontrado, retorna 200 OK com o remédio no corpo
                .orElse(ResponseEntity.notFound().build()); // Se o remédio não for encontrado, retorna 404 Not Found
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Remedio> atualizar(@PathVariable Long id, @RequestBody @Valid RemedioAtualizarDto dados) {
        Remedio remedioAtualizado = remedioService.atualizar(id, dados).orElse(null);
        if (remedioAtualizado != null) {
            return ResponseEntity.ok(remedioAtualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        remedioService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    // Método para buscar pelo nome do medicamento
    @GetMapping("/buscar")
    public ResponseEntity<List<Remedio>> buscarPorNome(@RequestParam String nome) {
        List<Remedio> remedios = remedioService.buscarPorNome(nome);
        if (remedios.isEmpty()) {
            return ResponseEntity.noContent().build(); // Retorna 204 No Content se nenhum remédio for encontrado com o nome
        } else {
            return ResponseEntity.ok(remedios); // Retorna 200 OK com a lista de remédios encontrados
        }
    }


}
