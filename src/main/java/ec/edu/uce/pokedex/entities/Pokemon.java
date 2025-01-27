package ec.edu.uce.pokedex.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Pokemon {
    //1025 pokemon registrados
    @Id
    private Integer id;

    private String name;
    private Integer height;
    private Integer weight;
    private String sprite;
    @ManyToMany(cascade = CascadeType.PERSIST)  // Se agrega la cascada para guardar los tipos asociados
    @JoinTable(
            name = "pokemon_type",
            joinColumns = @JoinColumn(name = "pokemon_id"),
            inverseJoinColumns = @JoinColumn(name = "type_id")
    )
    private List<Type> type;
    private StringBuilder types;
    private String generation;

    public StringBuilder getTypes() {
      return types;
    }

    public void setTypes(StringBuilder types) {
        this.types = types;
    }

    public String getGeneration() {
        return generation;
    }

    public void setGeneration(String generation) {
        this.generation = generation;
    }

    public List<Type> getType() {
        return type;
    }

    public void setType(List<Type> type) {

        this.type = type;
    }

    public String getSprite() {
        return sprite;
    }

    public void setSprite(String sprite) {
        this.sprite = sprite;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }
}
