package ec.edu.uce.pokedex.pokeapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import ec.edu.uce.pokedex.entities.PokemonSpecies;

import java.util.List;

public class GenerationResponse {
    private String name;
    @JsonProperty("pokemon_species")
    private List<PokemonSpecies> pokemon_species;

    // Getters y setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PokemonSpecies> getPokemonSpecies() {
        return pokemon_species;
    }

    public void setPokemonSpecies(List<PokemonSpecies> pokemon_species) {
        this.pokemon_species = pokemon_species;
    }

}
