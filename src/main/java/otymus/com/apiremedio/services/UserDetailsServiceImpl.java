package otymus.com.apiremedio.services;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import otymus.com.apiremedio.entities.Role;
import otymus.com.apiremedio.entities.Usuario;
import otymus.com.apiremedio.repositories.UsuarioRepository;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o login: " + login));

        // Declare 'authorities' fora do bloco if
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        // Supondo que sua entidade Usuario tenha um relacionamento ManyToMany com uma entidade Permissao
        // e que a entidade Permissao tenha um campo 'nome' (ex: "ROLE_ADMIN", "READ_MEDICAMENTOS", "WRITE_PACIENTES").
        if (usuario.getRoles() != null && !usuario.getRoles().isEmpty()) {
            authorities.addAll(usuario.getRoles().stream()
                    .map(Role::getNome) // Assumindo que a entidade Permissao tem um método getNome()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList()));
        }

        return new User(usuario.getLogin(), usuario.getSenha(), authorities);
    }
}