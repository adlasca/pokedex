package ec.edu.uce.pokedex.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Type {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "type")
    private List<Pokemon> pokemons;

    // Constructor para inicializar el nombre
    public Type(String name) {
        this.name = name;
    }

    // Constructor vacío para JPA
    public Type() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Pokemon> getPokemons() {
        return pokemons;
    }

    public void setPokemons(List<Pokemon> pokemons) {
        this.pokemons = pokemons;
    }
}
