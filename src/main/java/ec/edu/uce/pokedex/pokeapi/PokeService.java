package ec.edu.uce.pokedex.pokeapi;

import ec.edu.uce.pokedex.entities.Pokemon;
import ec.edu.uce.pokedex.entities.Type;
import ec.edu.uce.pokedex.repositories.PokemonRepository;
import ec.edu.uce.pokedex.repositories.TypeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/// Clase encargada de conectarse con la API y guardar en la base de datos
@Service
public class PokeService {

    @Autowired
    private WebClient webClient;
    @Autowired
    private PokemonRepository pokemonRepository;
    @Autowired
    private TypeRepository typeRepository;
    private Executor executor;

    /// El metodo principal en el que se basa la recoleccion de datos iniciada por generacion de pokemon
    public void fetchPokemonByGeneration() {
        executor = Executors.newCachedThreadPool();
    /// Stream para recolectar los datos segun un rango de numeros (1-9), el numero de generaciones existentes
    /// en la API
        IntStream.rangeClosed(1, 9).forEach(generationId -> {
            executor.execute(() -> {
                try {
                    /// Llamada al metodo que obtiene los datos de cada generacion
                    processGeneration(generationId);
                } catch (Exception e) {
                    System.err.println("Error al procesar la generación: " + generationId);
                    e.printStackTrace();
                }
            });
        });
    }

///Metodo que obtiene los tipos y especies de pokemon del Json generation de la API
    private void processGeneration(int generationId) {
        try {
            // Obtener la información de la generación
            GenerationResponse response = webClient.get()
                    .uri(uriBuilder -> uriBuilder.path("generation/{id}").build(generationId))
                    .retrieve()
                    .bodyToMono(GenerationResponse.class)
                    .block();
            if (response != null && response.getPokemonSpecies() != null) {
                /// Lista de los tipos de pokemon
                List<Type> pokemonTypes = processTypes(response);
                /// Lista de los pokemon
                List<Pokemon> pokemonList = processPokemons(generationId, response, pokemonTypes);
                /// Guardar Pokémon y tipos en la base de datos
                saveEntities(pokemonList, pokemonTypes);
            }
        } catch (Exception e) {
            System.err.println("Error al procesar la generación: " + generationId);
            e.printStackTrace();
        }
    }

/// Obtiene la lista de todos los pokemon por cada generacion
    private List<Pokemon> processPokemons(int generationId, GenerationResponse response, List<Type> pokemonTypes) {
        List<Pokemon> pokemonList = new ArrayList<>();
        for (var species : response.getPokemonSpecies()) {
            String pokemonName = species.getName();
            /// Se extrae el id desde la url
            int pokemonId = extractIdFromUrl(species.getUrl());

            PokeApiResponse pokemonData = webClient.get()
                    .uri(uriBuilder -> uriBuilder.path("pokemon/{id}").build(pokemonId))
                    .retrieve()
                    .bodyToMono(PokeApiResponse.class)
                    .block();

            if (pokemonData != null) {
                Pokemon pokemon = new Pokemon();
                pokemon.setId(pokemonId);
                pokemon.setName(pokemonName);
                pokemon.setHeight(pokemonData.getHeight());
                pokemon.setWeight(pokemonData.getWeight());
                pokemon.setSprite(pokemonData.getSprites().getFrontDefault());

                /// Asociar los tipos a cada pokemon
                List<Type> types = pokemonData.getTypes().stream()
                        .map(typeSlot -> typeRepository.findByName(typeSlot.getType().getName()))
                        .filter(Objects::nonNull)
                        .toList();

                pokemon.setType(types);
                pokemon.setGeneration("generation-" + generationId);
                pokemonList.add(pokemon);
            }
        }
        return pokemonList;
    }

/// Genera una lista de tipos de pokemon por cada generacion
    private List<Type> processTypes(GenerationResponse response) {
        List<Type> pokemonTypes = new ArrayList<>();
       /// Obtener los tipos de pokemon desde la API
        for (var types : response.getTypes()) {
            int typeId = extractIdFromUrl(types.getUrl());
            /// Comprueba si en la base de datos existe el tipo
            Type existingType = typeRepository.findById(typeId).orElse(null);
/// Si no existe el tipo, se busca en la API
            if (existingType == null) {
               TypeResponse typeData = webClient.get()
                        .uri(uriBuilder -> uriBuilder.path("type/{id}").build(typeId))
                        .retrieve()
                        .bodyToMono(TypeResponse.class)
                        .block();
               /// Agregar el id, nombre de los tipos y relacionarlos con los pokemon
                if (typeData != null) {
                    Type newType = new Type();
                    newType.setId(typeId);
                    newType.setName(typeData.getName());
                    typeRepository.save(newType);
                    pokemonTypes.add(newType);
                }
            } else {
                pokemonTypes.add(existingType);
            }
        }
        return pokemonTypes;
    }
/// Metodo para separar el id de la url por ejemplo :https://pokeapi.co/api/v2/generation/1 el cual separa el numero
    private int extractIdFromUrl(String url) {
        String[] parts = url.split("/");
        return Integer.parseInt(parts[parts.length - 1]);
    }

    /// Guarda la ista de pokemon y tipos en la base de datos
    private void saveEntities(List<Pokemon> pokemonList, List<Type> pokemonTypes) {
        pokemonRepository.saveAll(pokemonList);
        typeRepository.saveAll(pokemonTypes);
    }
}
