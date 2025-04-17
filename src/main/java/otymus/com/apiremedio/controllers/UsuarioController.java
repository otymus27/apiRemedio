package otymus.com.apiremedio.controllers;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import otymus.com.apiremedio.entities.Role;
import otymus.com.apiremedio.entities.Usuario;
import otymus.com.apiremedio.entities.dto.UsuarioCadastrarDto;
import otymus.com.apiremedio.repositories.RoleRepository;
import otymus.com.apiremedio.repositories.UsuarioRepository;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/usuarios") // Padroniza o caminho base da sua API
@EnableMethodSecurity
public class UsuarioController {
    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);


    @Autowired
    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UsuarioController(UsuarioRepository usuarioRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder){
        this.usuarioRepository = usuarioRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping()
    @Transactional
    public ResponseEntity<Void> cadastrar(@RequestBody UsuarioCadastrarDto dados) {
        logger.info("Dados recebidos para cadastro: {}", dados); // ADICIONE ESTA LINHA

        var basicRoleOptimal = roleRepository.findByNome(Role.Values.BASIC.name());
        System.out.println(basicRoleOptimal);

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

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<Usuario>> listar() {
        var lista = usuarioRepository.findAll();
        return ResponseEntity.ok(lista);
    }
}
