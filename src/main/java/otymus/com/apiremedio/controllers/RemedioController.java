package otymus.com.apiremedio.controllers;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
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

    @Autowired
    private RemedioRepository remedioRepository;

    private RemedioService remedioService;
    public RemedioController(RemedioService remedioService) {
        this.remedioService = remedioService;
    }

    @PostMapping
    @Transactional // Garante a atomicidade da operação no banco de dados
    public ResponseEntity<Remedio> cadastrar(@RequestBody @Valid RemedioCadastrarDto dados, UriComponentsBuilder uriBuilder) {
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
    @Transactional // Garante a atomicidade da operação no banco de dados
    public ResponseEntity<Remedio> atualizar(@PathVariable Long id, @RequestBody @Valid RemedioAtualizarDto dados) {
        return remedioService.atualizar(id, dados)
                .map(ResponseEntity::ok) // Se o remédio for encontrado e atualizado, retorna 200 OK com o remédio atualizado
                .orElse(ResponseEntity.notFound().build()); // Se o remédio não for encontrado, retorna 404 Not Found
    }

    @DeleteMapping("/{id}")
    @Transactional // Garante a atomicidade da operação no banco de dados
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        boolean excluido = remedioService.excluir(id);
        if (excluido) {
            return ResponseEntity.noContent().build(); // Retorna 204 No Content se a exclusão for bem-sucedida
        } else {
            return ResponseEntity.notFound().build(); // Retorna 404 Not Found se o remédio não for encontrado
        }
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
