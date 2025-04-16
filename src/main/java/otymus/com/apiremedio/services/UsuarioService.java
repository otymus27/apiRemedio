package otymus.com.apiremedio.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import otymus.com.apiremedio.entities.Usuario;
import otymus.com.apiremedio.exceptions.DadosInvalidosException;
import otymus.com.apiremedio.repositories.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario cadastrar(Usuario usuario) {
        // Adicione validações de negócio aqui, se necessário
        if (usuario.getLogin() == null || usuario.getLogin().trim().isEmpty()) {
            throw new DadosInvalidosException("O nome do login não pode ser vazio.");
        }
        return usuarioRepository.save(usuario);
    }

}
