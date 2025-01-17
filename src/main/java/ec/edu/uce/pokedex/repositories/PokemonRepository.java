package ec.edu.uce.pokedex.repositories;

import ec.edu.uce.pokedex.entities.Pokemon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PokemonRepository extends JpaRepository<Pokemon, Integer> {


}
