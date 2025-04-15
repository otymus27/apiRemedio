package otymus.com.apiremedio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import otymus.com.apiremedio.entities.Remedio;

import java.util.List;

// Aqui já tenho tudo necessário para manipulação no banco de dados
public interface RemedioRepository extends JpaRepository<Remedio, Long> {
    List<Remedio> findByNomeContainingIgnoreCase(String nome);
}
