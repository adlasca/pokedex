package ec.edu.uce.pokedex.pokeapi;

/// Clase que obtiene el nombre y el id de los tipos de pokemon
public class TypeResponse {

    private String name;
    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
