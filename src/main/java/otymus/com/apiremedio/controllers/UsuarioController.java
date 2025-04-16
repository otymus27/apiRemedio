package otymus.com.apiremedio.controllers;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import otymus.com.apiremedio.entities.Role;
import otymus.com.apiremedio.entities.Usuario;
import otymus.com.apiremedio.entities.dto.UsuarioCadastrarDto;
import otymus.com.apiremedio.repositories.RoleRepository;
import otymus.com.apiremedio.repositories.UsuarioRepository;

import java.util.Set;

@RestController
public class UsuarioController {
    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);


    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UsuarioController(UsuarioRepository usuarioRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder){
        this.usuarioRepository = usuarioRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/usuarios")
    @Transactional
    public ResponseEntity<Void> cadastrar(UsuarioCadastrarDto dados) {
        logger.info("Dados recebidos para cadastro: {}", dados); // ADICIONE ESTA LINHA

        var basicRoleOptimal = roleRepository.findByNome(Role.Values.BASIC.name());

        if (basicRoleOptimal.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Role 'BASIC' não encontrada no banco de dados.");
        }
        var basicRole = basicRoleOptimal.get();

        var usuarioFromDb = usuarioRepository.findByLogin(dados.login());
        if (usuarioFromDb.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Usuário já cadastrado.");
        }

        var usuario = new Usuario();
        usuario.setLogin(dados.login());
        usuario.setSenha(passwordEncoder.encode(dados.senha()));
        usuario.setRoles(Set.of(basicRole));

        usuarioRepository.save(usuario);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
