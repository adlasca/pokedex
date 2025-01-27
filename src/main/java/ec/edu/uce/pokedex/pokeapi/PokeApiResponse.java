package ec.edu.uce.pokedex.pokeapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import ec.edu.uce.pokedex.entities.Type;

import java.util.List;

/// Clase que funciona para obtener los datos de cada pokemon en la API

public class PokeApiResponse  {

    private Integer id;
    private String name;
    private int weight;
    private int height;
    private SpriteResponse sprites;
    private List<TypeSlot> types;

    public List<TypeSlot> getTypes() {
        return types;
    }

    public void setTypes(List<TypeSlot> types) {
        this.types = types;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public SpriteResponse getSprites() {
        return sprites;
    }

    public void setSprites(SpriteResponse sprites) {
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

}
