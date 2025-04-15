package otymus.com.apiremedio.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import otymus.com.apiremedio.entities.Remedio;
import otymus.com.apiremedio.entities.dto.RemedioAtualizarDto;
import otymus.com.apiremedio.repositories.RemedioRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RemedioService {
    @Autowired
    private RemedioRepository remedioRepository;

    public RemedioService(RemedioRepository remedioRepository) {
        this.remedioRepository = remedioRepository;
    }

    public List<Remedio> listar() {
        return remedioRepository.findAll();
    }

    public Optional<Remedio> buscarPorId(Long id) {
        return remedioRepository.findById(id);
    }

    public Remedio cadastrar(Remedio item) {
        return remedioRepository.save(item);
    }

    @Transactional
    public Optional<Remedio> atualizar(Long id, RemedioAtualizarDto dados) {
        return remedioRepository.findById(id)
                .map(remedio -> {
                    remedio.atualizarDados(dados); // Método na entidade para atualizar os dados
                    return remedioRepository.save(remedio);
                });
    }

    @Transactional
    public boolean excluir(Long id) {
        return remedioRepository.findById(id)
                .map(remedio -> {
                    remedioRepository.delete(remedio);
                    return true; // Indica que a exclusão foi bem-sucedida
                })
                .orElse(false); // Indica que o remédio não foi encontrado
    }

    public List<Remedio> buscarPorNome(String nome) {
        return remedioRepository.findByNomeContainingIgnoreCase(nome);
    }
}
