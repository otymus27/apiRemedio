package otymus.com.apiremedio;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestePasswordEncoderUtil {
    public static void main(String[] args) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String senhaParaCodificar = "123";
        String senhaCodificada = passwordEncoder.encode(senhaParaCodificar);
        System.out.println("Senha codificada: " + senhaCodificada);
    }
}
