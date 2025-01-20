package ec.edu.uce.pokedex.pokeapi;

import ec.edu.uce.pokedex.entities.Pokemon;
import ec.edu.uce.pokedex.repositories.PokemonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
                    pokemon.setType(response.getTypes());

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

    public void fetchPokemonByGeneration() {
        IntStream.rangeClosed(1, 9).parallel().forEach(generationId -> {
            String generationName = "generation-" + generationId;

            try {
                // Llamar al endpoint de generación
                GenerationResponse response = webClient.get()
                        .uri(uriBuilder -> uriBuilder.path("generation/{id}").build(generationId))
                        .retrieve()
                        .bodyToMono(GenerationResponse.class)
                        .block();

                if (response != null && response.getPokemonSpecies() != null) {
                    // Lista para almacenar todos los Pokémon de esta generación
                    List<Pokemon> pokemonList = new ArrayList<>();

                    for (var species : response.getPokemonSpecies()) {
                        try {
                            String pokemonName = species.getName();
                            int pokemonId = extractIdFromUrl(species.getUrl());

                            // Obtener los datos del Pokémon
                            PokeApiResponse pokemonData = webClient.get()
                                    .uri(uriBuilder -> uriBuilder.path("pokemon/{id}").build(pokemonId))
                                    .retrieve()
                                    .bodyToMono(PokeApiResponse.class)
                                    .block();

                            if (pokemonData != null) {
                                // Crear el objeto Pokémon
                                Pokemon pokemon = new Pokemon();
                                pokemon.setId(pokemonId);
                                pokemon.setName(pokemonData.getName());
                                pokemon.setHeight(pokemonData.getHeight());
                                pokemon.setWeight(pokemonData.getWeight());
                                pokemon.setSprite(pokemonData.getSprites().getFrontDefault());
                                pokemon.setType(pokemonData.getTypes());
                                pokemon.setGeneration(generationName);

                                // Agregar a la lista
                                pokemonList.add(pokemon);
                            }
                        } catch (Exception e) {
                            System.err.println("Error al procesar Pokémon: " + species.getName());
                            e.printStackTrace();
                        }
                    }

                    // Guardar todos los Pokémon de esta generación en un solo paso
                    synchronized (pokemonRepository) {
                        pokemonRepository.saveAll(pokemonList);
                    }
                }
            } catch (Exception e) {
                System.err.println("Error al procesar la generación: " + generationId);
                e.printStackTrace();
            }
        });
    }

    private int extractIdFromUrl(String url) {
        String[] parts = url.split("/");
        return Integer.parseInt(parts[parts.length - 1]);
    }

}
