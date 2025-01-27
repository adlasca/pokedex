package ec.edu.uce.pokedex.pokeapi;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SpriteResponse {
    @JsonProperty("front_default")
    private String frontDefault;

    public String getFrontDefault() {
        return frontDefault;
    }

    public void setFrontDefault(String frontDefault) {
        this.frontDefault = frontDefault;
    }
}
