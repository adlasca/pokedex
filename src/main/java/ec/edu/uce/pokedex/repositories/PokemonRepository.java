package ec.edu.uce.pokedex.repositories;

import ec.edu.uce.pokedex.entities.Pokemon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PokemonRepository extends PagingAndSortingRepository<Pokemon, Integer>, JpaRepository<Pokemon, Integer> {

    List<Pokemon> findByName(String name);
}
