package ec.edu.uce.pokedex.ui;

import ec.edu.uce.pokedex.entities.Pokemon;
import ec.edu.uce.pokedex.repositories.PokemonRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.net.URL;
import java.util.List;

public class PokeUI extends JFrame {

    private JTable pokemonTable;
    private DefaultTableModel tableModel;
    private JLabel spriteLabel;
    private int currentPage = 0;  // Página actual
    private int pageSize = 20;// Número de Pokémon por página
    private JButton nextButton;
    private JButton prevButton;
    private PokemonRepository pokemonRepository;

    public PokeUI(PokemonRepository pokemonRepository) {
        this.pokemonRepository = pokemonRepository;
        try{

            // Configuración de la ventana
            setTitle("Pokedex");
            setSize(800, 600);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLayout(new BorderLayout());

            // Configuración de la tabla
            tableModel = new DefaultTableModel(new Object[]{"ID", "Nombre", "Altura", "Peso", "Sprite URL"}, 0);
            pokemonTable = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(pokemonTable);
            add(scrollPane, BorderLayout.CENTER);

            // Configuración de la etiqueta para mostrar el sprite
            spriteLabel = new JLabel();
            spriteLabel.setHorizontalAlignment(SwingConstants.CENTER);
            add(spriteLabel, BorderLayout.SOUTH);

            // Listener para seleccionar una fila y mostrar el sprite
            pokemonTable.getSelectionModel().addListSelectionListener(e -> {
                int selectedRow = pokemonTable.getSelectedRow();
                if (selectedRow != -1) {
                    String spriteUrl = (String) tableModel.getValueAt(selectedRow, 4);
                    showSprite(spriteUrl);
                }
            });

            // Configuración de los botones para navegación de páginas
            JPanel paginationPanel = new JPanel();
            prevButton = new JButton("Anterior");
            nextButton = new JButton("Siguiente");
            paginationPanel.add(prevButton);
            paginationPanel.add(nextButton);
            add(paginationPanel, BorderLayout.NORTH);

            prevButton.addActionListener(e -> loadPokemonDataFromDatabase(currentPage - 1));
            nextButton.addActionListener(e -> loadPokemonDataFromDatabase(currentPage + 1));

            // Cargar Pokémon desde la base de datos en un hilo separado
            loadPokemonDataFromDatabase(currentPage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadPokemonDataFromDatabase(int page) {
        // Establecer la página y el tamaño de la página
        Pageable pageable = PageRequest.of(page, pageSize);

        // Obtener los Pokémon desde la base de datos con paginación
        Page<Pokemon> pokemonPage = pokemonRepository.findAll(pageable);

        // Limpiar la tabla antes de agregar los nuevos Pokémon
        tableModel.setRowCount(0);

        // Agregar los Pokémon de la página actual a la tabla
        List<Pokemon> pokemonList = pokemonPage.getContent();
        pokemonList.forEach(pokemon -> {
            tableModel.addRow(new Object[]{
                    pokemon.getId(),
                    pokemon.getName(),
                    pokemon.getHeight(),
                    pokemon.getWeight(),
                    pokemon.getSprite()  // Suponiendo que tienes la URL del sprite guardada
            });
        });

        // Actualizar la página actual
        this.currentPage = page;

        // Habilitar o deshabilitar los botones de paginación según la página actual
        if (page == 0) {
            prevButton.setEnabled(false);
        } else {
            prevButton.setEnabled(true);
        }

        if (pokemonPage.hasNext()) {
            nextButton.setEnabled(true);
        } else {
            nextButton.setEnabled(false);
        }
    }

    private void showSprite(String spriteUrl) {
        try {
            ImageIcon spriteIcon = new ImageIcon(new URL(spriteUrl));
            Image scaledImage = spriteIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            spriteLabel.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen del sprite: " + e.getMessage());
        }
    }
}
