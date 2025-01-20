package ec.edu.uce.pokedex.pokeapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import ec.edu.uce.pokedex.entities.Type;

import java.util.List;

public class PokeApiResponse {

    private Integer id;
    private String name;
    private int weight;
    private int height;
    private Sprites sprites;
    private List<Type> types;

    // Getters y Setters


    public List<Type> getTypes() {
        return types;
    }

    public void setTypes(List<Type> types) {
        this.types = types;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Sprites getSprites() {
        return sprites;
    }

    public void setSprites(Sprites sprites) {
        this.sprites = sprites;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    // Clase interna para manejar sprites
    public static class Sprites {
        @JsonProperty("front_default")
        private String frontDefault;

        public String getFrontDefault() {
            return frontDefault;
        }

        public void setFrontDefault(String frontDefault) {
            this.frontDefault = frontDefault;
        }
    }
}
