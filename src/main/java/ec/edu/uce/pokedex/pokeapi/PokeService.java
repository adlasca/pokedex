package ec.edu.uce.pokedex.pokeapi;

import ec.edu.uce.pokedex.entities.Pokemon;
import ec.edu.uce.pokedex.repositories.PokemonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

import java.net.URL;
import java.util.stream.IntStream;
@Service
public class PokeService {

    @Autowired
    private WebClient webClient;
    @Autowired
    private PokemonRepository pokemonRepository;

    public void fetchAndSavePokemon() {

        IntStream.rangeClosed(1,1025).parallel().forEach(id->{
            try {
                URL url = new URL("https://pokeapi.co/api/v2/pokemon/"+id);

                PokeApiResponse response = webClient.get()
                        .uri(uriBuilder -> uriBuilder.path("pokemon/{id}").build(id))
                        .retrieve()
                        .bodyToMono(PokeApiResponse.class)
                        .block();

                if(response!=null){
                    Pokemon pokemon =new Pokemon();
                    pokemon.setId(id);
                    pokemon.setName(response.getName());
                    pokemon.setHeight(response.getHeight());
                    pokemon.setWeight(response.getWeight());
                    pokemonRepository.save(pokemon);
                }
            }catch (WebClientException w){
                System.err.println(w.getMessage());
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }


}
