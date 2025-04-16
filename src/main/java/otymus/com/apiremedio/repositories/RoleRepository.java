package otymus.com.apiremedio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import otymus.com.apiremedio.entities.Role;
import otymus.com.apiremedio.entities.Usuario;

import java.util.Optional;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByNome(String nome);
}
