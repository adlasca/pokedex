package ec.edu.uce.pokedex.pokeapi;

import ec.edu.uce.pokedex.entities.Pokemon;
import ec.edu.uce.pokedex.repositories.PokemonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

import java.net.URL;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
public class PokeService {

    @Autowired
    private WebClient webClient;
    @Autowired
    private PokemonRepository pokemonRepository;

    public void fetchAndSavePokemon() {

        Stream<Integer> pokemonIds = Stream.concat(
                IntStream.rangeClosed(1, 1025).boxed(),
                IntStream.rangeClosed(10001, 10277).boxed()
        );
        long startTime = System.currentTimeMillis();
//10001--10277 __1025
        pokemonIds.parallel().forEach(id->{
            try {
                URL url = new URL("https://pokeapi.co/api/v2/");

                PokeApiResponse response = webClient.get()
                        .uri(uriBuilder -> uriBuilder.path("pokemon/{id}").build(id))
                        .retrieve()
                        .bodyToMono(PokeApiResponse.class)
                        .block();

                if(response!=null){
                    // Crear y guardar el Pokémon
                    Pokemon pokemon = new Pokemon();
                    pokemon.setId(id);
                    pokemon.setName(response.getName());
                    pokemon.setHeight(response.getHeight());
                    pokemon.setWeight(response.getWeight());

                    // Obtener la URL del sprite
                    String spriteUrl = response.getSprites().getFrontDefault();
                    pokemon.setSprite(spriteUrl);

                    synchronized (pokemonRepository) {
                        pokemonRepository.save(pokemon);
                    }
                }
            }catch (WebClientException w){
                System.err.println(w.getMessage());
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        long seconds = duration / 1000;
        long milliseconds = duration % 1000;

        System.out.println("Tiempo total de inserción: " + seconds + " segundos y " + milliseconds + " milisegundos.");

    }


}
