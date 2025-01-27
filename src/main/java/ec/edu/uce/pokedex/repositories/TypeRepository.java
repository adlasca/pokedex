package ec.edu.uce.pokedex.repositories;

import ec.edu.uce.pokedex.entities.Type;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TypeRepository extends JpaRepository<Type, Integer> {

    Type findByName(String typeName);
}
