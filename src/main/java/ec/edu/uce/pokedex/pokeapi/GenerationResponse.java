package ec.edu.uce.pokedex.pokeapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import ec.edu.uce.pokedex.entities.PokemonSpecies;

import java.util.List;

/// CLASE QUE FUNCIONA PARA OBTENER EL NOMBRE DE LA GENERACION, LAS ESPECIES DE POKÉMON Y LOS TIPOS
/// QUE SE ENCUENTRAN EN CADA GENERACION
/// LO QUE EN LA API CORRESPONDE A:  {"name":"generation","pokemon_species":[{...}],"types":[{...}]}
public class GenerationResponse {
    private String name;
    @JsonProperty("pokemon_species")
    private List<PokemonSpecies> pokemon_species;

    private List<TypeTypes> types;

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

    public List<TypeTypes> getTypes() {
        return types;
    }

    public void setTypes(List<TypeTypes> types) {
        this.types = types;
    }
}
