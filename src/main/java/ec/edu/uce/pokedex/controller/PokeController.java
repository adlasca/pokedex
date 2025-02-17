package ec.edu.uce.pokedex.controller;

import ec.edu.uce.pokedex.entities.Pokemon;
import ec.edu.uce.pokedex.pokeapi.PokeService;
import ec.edu.uce.pokedex.repositories.PokemonRepository;
import ec.edu.uce.pokedex.repositories.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class PokeController {
    private PokeService pokeService;
    @Autowired
    PokemonRepository pokemonRepository;

    public PokeController(PokeService pokeService) {
        this.pokeService = pokeService;
    }

    @GetMapping("/")
    public String getPokemonList(@RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "20") int size,
                                 @RequestParam(required = false) String search,
                                 @RequestParam(required = false) String generation,
                                 @RequestParam(required = false) String type,
                                 Model model) {

        List<Pokemon> pokemonList;

        if (search != null && !search.isEmpty()) {
            pokemonList = pokemonRepository.findByName(search);
        } else if (generation != null && !generation.isEmpty()) {
            pokemonList = pokemonRepository.findByGeneration(generation);
        } else if (type != null && !type.isEmpty()) {
            pokemonList = pokemonRepository.findByTypeName(type);
        } else {
            // Paginación optimizada
            int start = (page - 1) * size + 1;
            int end = start + size - 1;

            List<Integer> ids = new ArrayList<>();
            for (int i = start; i <= end; i++) {
                ids.add(i);
            }
            pokemonList = pokemonRepository.findAllById(ids);
        }

        model.addAttribute("pokemonList", pokemonList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", 50);
        model.addAttribute("pageSize", size);
        model.addAttribute("selectedGeneration", generation);
        model.addAttribute("generation", Arrays.asList("generation-1", "generation-2", "generation-3", "generation-4", "generation-5", "generation-6", "generation-7", "generation-8", "generation-9"));
        model.addAttribute("selectedType", type);
        model.addAttribute("type", Arrays.asList("normal","fighting","flying" ,"poison", "ground","rock","bug","ghost","steel","fire","water","grass","electric","psychic","ice","dragon","dark","fairy"));

        return "index";
    }
}
