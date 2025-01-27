package ec.edu.uce.pokedex.pokeapi;

/// Clase que obtiene el nombre y el url del campo types que contiene cada pokemon en la API parte de TypeSlot
public class TypeInfoResponse {
    private String name;
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
