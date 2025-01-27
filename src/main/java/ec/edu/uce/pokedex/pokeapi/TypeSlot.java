package ec.edu.uce.pokedex.pokeapi;

/// Obtiene los datos slot y type del campo types del Json pokemon
public class TypeSlot {
    private int slot;
    private TypeInfoResponse type;

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public TypeInfoResponse getType() {
        return type;
    }

    public void setType(TypeInfoResponse type) {
        this.type = type;
    }
}
