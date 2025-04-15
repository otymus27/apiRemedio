package otymus.com.apiremedio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import otymus.com.apiremedio.entities.Role;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
