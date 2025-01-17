package ec.edu.uce.pokedex.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebConfig {
@Bean
    public WebClient webClient(WebClient.Builder builder) {
    return builder
            .baseUrl("https://pokeapi.co/api/v2/")
            .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024)) // Aumentar el límite a 16 MB
            .build();
    }
}
