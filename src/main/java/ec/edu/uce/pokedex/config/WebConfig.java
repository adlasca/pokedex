package ec.edu.uce.pokedex.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebConfig {

    /// Uso de WebClient de spring para las solicitudes HTTP
    /// Contiene la configuracion inicial, como la url principal de la API y un aumento de tamaño en la memoria para guardar mas datos
@Bean
    public WebClient webClient(WebClient.Builder builder) {
    return builder
            .baseUrl("https://pokeapi.co/api/v2/")
            .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024)) // Aumentar el límite a 16 MB
            .build();
    }
}
