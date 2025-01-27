package ec.edu.uce.pokedex.entities;

import jakarta.persistence.*;

import java.util.List;
/// Tabla que contiene todos los tipos de pokemon
@Entity
public class Type {

    @Id
    private int id;

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToMany(mappedBy = "type")
    private List<Pokemon> pokemons;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
