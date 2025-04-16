package otymus.com.apiremedio.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import otymus.com.apiremedio.entities.Role;
import otymus.com.apiremedio.entities.dto.LoginRequest;
import otymus.com.apiremedio.entities.dto.LoginResponse;
import otymus.com.apiremedio.repositories.UsuarioRepository;

import java.time.Instant;
import java.util.stream.Collectors;

@RestController
public class TokenController {

    private final JwtEncoder jwtEncoder;
    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public TokenController(JwtEncoder jwtEncoder, UsuarioRepository usuarioRepository, BCryptPasswordEncoder passwordEncoder){
        this.jwtEncoder = jwtEncoder;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login (@RequestBody LoginRequest loginRequest) {
        var user = usuarioRepository.findByLogin(loginRequest.login());

        if (user.isEmpty() || !user.get().isLoginCorrect(loginRequest, passwordEncoder)) {
            throw new BadCredentialsException("usuario ou senha invalidos!!!");
        }

        var now = Instant.now();
        var expiresIn = 300L;

        var scopes = user.get().getRoles()
                .stream()
                .map(Role::getNome)
                .collect(Collectors.joining(" "));

        var claims = JwtClaimsSet.builder()
                .issuer("meuBackend")
                .subject(user.get().getLogin())
                .expiresAt(now.plusSeconds(expiresIn))
                .issuedAt(now)
                .claim("scope", scopes)
                .build();
        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return  ResponseEntity.ok(new LoginResponse(jwtValue, expiresIn));
    }

}
