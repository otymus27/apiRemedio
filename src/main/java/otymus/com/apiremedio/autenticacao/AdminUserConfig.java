package otymus.com.apiremedio.autenticacao;

import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import otymus.com.apiremedio.entities.Role;
import otymus.com.apiremedio.entities.Usuario;
import otymus.com.apiremedio.repositories.RoleRepository;
import otymus.com.apiremedio.repositories.UsuarioRepository;
import jakarta.persistence.EntityManager;

import java.util.Set;

@Configuration
@Component
public class AdminUserConfig implements CommandLineRunner {

    private RoleRepository roleRepository;
    private UsuarioRepository usuarioRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private final EntityManager entityManager; // Inject EntityManager

    public AdminUserConfig(RoleRepository roleRepository, UsuarioRepository usuarioRepository, BCryptPasswordEncoder passwordEncoder, EntityManager entityManager) {
        this.roleRepository = roleRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.entityManager = entityManager;

    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        var roleAdminOptional = roleRepository.findByNome(Role.Values.ADMIN.name());
        Role roleAdmin;

        if (roleAdminOptional.isEmpty()) {
            roleAdmin = new Role();
            roleAdmin.setNome(Role.Values.ADMIN.name());
            roleRepository.save(roleAdmin);
            System.out.println("Role 'ADMIN' criada.");
        } else {
            roleAdmin = entityManager.merge(roleAdminOptional.get()); // Merge the detached entity
            System.out.println("Role 'ADMIN' já existia e foi merged.");
        }

        var userAdmin = usuarioRepository.findByLogin("admin");
        userAdmin.ifPresentOrElse(
                (usuario) ->{
                    System.out.println("Admin ja existe");
                },
                () ->{
                    var usuario = new Usuario();
                    usuario.setLogin("admin");
                    usuario.setSenha(passwordEncoder.encode("123"));
                    usuario.setRoles(Set.of(roleAdmin));
                    usuarioRepository.save(usuario);
                    System.out.println("Admin criado.");
                }
        );
    }
}
