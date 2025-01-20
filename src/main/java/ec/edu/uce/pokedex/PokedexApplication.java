package ec.edu.uce.pokedex;

import ec.edu.uce.pokedex.pokeapi.PokeService;
import ec.edu.uce.pokedex.repositories.PokemonRepository;
import ec.edu.uce.pokedex.ui.PokeUI;
import ec.edu.uce.pokedex.ui.PokedexUI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.*;
import java.awt.*;

@SpringBootApplication
public class PokedexApplication implements CommandLineRunner {

    @Autowired
    private PokeService pokeService;
    @Autowired
    private PokemonRepository pokemonRepository;


    public static void main(String[] args) {
        if (!GraphicsEnvironment.isHeadless()) {
            // Crear la interfaz gráfica si no estamos en un entorno headless
            System.setProperty("java.awt.headless", "false");
            SpringApplication.run(PokedexApplication.class, args);
        } else {
            System.out.println("No se puede ejecutar la interfaz gráfica en un entorno headless");
        }
    }

    @Override
    public void run(String... args) throws Exception {
        //pokeService.fetchAndSavePokemon();
        pokeService.fetchPokemonByGeneration();

        PokeUI pokeUI = new PokeUI(pokemonRepository);

        SwingUtilities.invokeLater(() -> {
            // Aquí se configura la visibilidad de la UI
            pokeUI.setVisible(true);
        });
    }
}
