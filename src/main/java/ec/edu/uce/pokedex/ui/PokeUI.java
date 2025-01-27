package ec.edu.uce.pokedex.ui;

import ec.edu.uce.pokedex.entities.Pokemon;
import ec.edu.uce.pokedex.entities.Type;
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

    private final PokemonRepository pokemonRepository;
    private JTextField searchTextField;
    private JButton searchButton;
    private JComboBox<String> generationComboBox;
    private DefaultTableModel tableModel;
    private JTable pokemonTable;
    private JLabel spriteLabel;
    private JLabel detailsLabel;
    private ImageIcon pokedexIcon;
    private JLabel pokedexLabel;

    public PokeUI(PokemonRepository pokemonRepository) {
        this.pokemonRepository = pokemonRepository;

        configureWindow();

        JPanel buscador = createSearchPanel();

        JPanel pokemonListPanel = createPokemonListPanel();

        JPanel detailsPanel = createDetailsPanel();

        /// Agregar componentes al layout principal
        add(buscador, BorderLayout.NORTH);
        add(pokemonListPanel, BorderLayout.EAST);
        add(detailsPanel, BorderLayout.CENTER);

        configureTableSelectionListener();
    }

    /// Configuración inicial de la ventana
    private void configureWindow() {
        setTitle("Pokedex");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocation(300,300);
        setResizable(false);
    }

    /// Configuración del buscador
    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchTextField = new JTextField();
        searchButton = new JButton("Buscar");
        searchPanel.add(searchTextField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        searchButton.addActionListener(e -> searchPokemon());
        return searchPanel;
    }

    /// Configuración del panel de lista de Pokémon
    private JPanel createPokemonListPanel() {
        JPanel pokemonListPanel = new JPanel(new BorderLayout());

        // Configuración del combo box de generación
        generationComboBox = new JComboBox<>(new String[]{
                "generation-1", "generation-2", "generation-3", "generation-4",
                "generation-5", "generation-6", "generation-7", "generation-8", "generation-9"
        });
        generationComboBox.addActionListener(e -> loadPokemonByGeneration((String) generationComboBox.getSelectedItem()));
        pokemonListPanel.add(generationComboBox, BorderLayout.NORTH);

        // Configuración de la tabla
        tableModel = new DefaultTableModel(new Object[]{"ID", "Nombre", "Tipo"}, 0);
        pokemonTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(pokemonTable);
        pokemonListPanel.add(scrollPane, BorderLayout.CENTER);

        return pokemonListPanel;
    }

    /// Panel para mostrar sprite y detalles
    private JPanel createDetailsPanel() {
        JPanel detailsPanel = new JPanel(new BorderLayout());
        spriteLabel = new JLabel("", SwingConstants.CENTER);
        detailsLabel = new JLabel("Detalles del Pokémon", SwingConstants.CENTER);
        pokedexIcon = new ImageIcon("images/pokedex_logo.png");
        pokedexLabel = new JLabel(pokedexIcon);
        Image pokedexImage = pokedexIcon.getImage().getScaledInstance(300, 150, Image.SCALE_SMOOTH);
        pokedexLabel.setIcon(new ImageIcon(pokedexImage));

        spriteLabel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY,3));
        detailsPanel.add(spriteLabel, BorderLayout.CENTER);
        detailsPanel.add(detailsLabel, BorderLayout.NORTH);
        detailsPanel.add(pokedexLabel, BorderLayout.SOUTH);
        detailsPanel.setBackground(Color.PINK);

        return detailsPanel;
    }

    /// Listener para la tabla de Pokémon
    private void configureTableSelectionListener() {
        pokemonTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = pokemonTable.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                showPokemonDetails(id);
            }
        });
    }

    private void loadPokemonByGeneration(String generation) {
        tableModel.setRowCount(0);
        List<Pokemon> pokemonList = pokemonRepository.findByGeneration(generation);
        for (Pokemon pokemon : pokemonList) {
            tableModel.addRow(new Object[]{
                    pokemon.getId(),
                    pokemon.getName(),
                    getPokemonTypesAsString(pokemon)
            });
        }
    }

    private void searchPokemon() {
        String searchQuery = searchTextField.getText().trim().toLowerCase();
        if (searchQuery.isEmpty()) {
            return;
        }
        tableModel.setRowCount(0);
        List<Pokemon> searchResults = pokemonRepository.findByName(searchQuery);
        for (Pokemon pokemon : searchResults) {
            tableModel.addRow(new Object[]{
                    pokemon.getId(),
                    pokemon.getName(),
                    getPokemonTypesAsString(pokemon)
            });
        }
    }

    private String getPokemonTypesAsString(Pokemon pokemon) {
        List<ec.edu.uce.pokedex.entities.Type> types = pokemon.getType();
        return types.stream()
                .map(type -> type.getName().toUpperCase())
                .reduce((a, b) -> a + ", " + b)
                .orElse("Sin tipo");
    }

    private void showPokemonDetails(int id) {
        Pokemon pokemon = pokemonRepository.findById(id);

        if (pokemon != null) {
            String details = String.format(
                    "<html>ID: %d<br>Nombre: %s<br>Peso: %d hg<br>Altura: %d dm</html>",
                    pokemon.getId(),
                    pokemon.getName().toUpperCase(),
                    pokemon.getWeight(),
                    pokemon.getHeight()
            );

            detailsLabel.setText(details);
            showPokemonSprite(pokemon.getSprite());
        }
    }

    private void showPokemonSprite(String spriteUrl) {
        try {
            if (spriteUrl != null && !spriteUrl.isEmpty()) {
                ImageIcon spriteIcon = new ImageIcon(new URL(spriteUrl));
                Image scaledImage = spriteIcon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
                spriteLabel.setIcon(new ImageIcon(scaledImage));
            } else {
                spriteLabel.setIcon(null);
                spriteLabel.setText("No hay sprite disponible");
            }
        } catch (Exception e) {
            spriteLabel.setIcon(null);
            spriteLabel.setText("Error al cargar la imagen del sprite");
        }
    }
}
