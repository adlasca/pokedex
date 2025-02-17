package ec.edu.uce.pokedex.repositories;

import ec.edu.uce.pokedex.entities.Pokemon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/// Se utiliza un query personalizado usando JOIN FETCH para evitar errores como el lazy  loading
@Repository
public interface PokemonRepository extends PagingAndSortingRepository<Pokemon, Integer>, JpaRepository<Pokemon, Integer> {
    @Query("SELECT DISTINCT p FROM Pokemon p LEFT JOIN FETCH p.type WHERE p.generation = :generation")
    List<Pokemon> findByGeneration(String generation);

    @Query("SELECT p FROM Pokemon p JOIN FETCH p.type WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Pokemon> findByName(String name);

    Pokemon findById(int id);

    @Query("SELECT p FROM Pokemon p JOIN FETCH p.type t WHERE t.name = :type")
    List<Pokemon> findByTypeName(String type);
}
