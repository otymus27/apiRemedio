package otymus.com.apiremedio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("otymus.com.apiremedio") // Escaneia o pacote base e seus subpacotes
public class ApiRemedioApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiRemedioApplication.class, args);
	}

}
