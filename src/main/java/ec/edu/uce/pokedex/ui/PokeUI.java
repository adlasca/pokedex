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
    private JLabel detailsLabel;
    private JComboBox<String> generationComboBox;

    private PokemonRepository pokemonRepository;

    public PokeUI(PokemonRepository pokemonRepository) {
        this.pokemonRepository = pokemonRepository;
        // Configuración de la ventana
        setTitle("Pokedex");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Configuración del combo box de generación
        generationComboBox = new JComboBox<>(new String[]{
                "generation-1", "generation-2", "generation-3", "generation-4",
                "generation-5", "generation-6", "generation-7", "generation-8", "generation-9"
        });
        generationComboBox.addActionListener(e -> loadPokemonByGeneration((String) generationComboBox.getSelectedItem()));
        add(generationComboBox, BorderLayout.NORTH);

        // Configuración de la tabla
        tableModel = new DefaultTableModel(new Object[]{"ID", "Nombre", "Altura", "Peso", "Sprite URL"}, 0);
        pokemonTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(pokemonTable);
        add(scrollPane, BorderLayout.CENTER);

        // Panel inferior para sprite y detalles
        JPanel bottomPanel = new JPanel(new BorderLayout());
        spriteLabel = new JLabel("Sprite aquí", SwingConstants.CENTER);
        bottomPanel.add(spriteLabel, BorderLayout.CENTER);

        detailsLabel = new JLabel("Detalles del Pokémon", SwingConstants.CENTER);
        bottomPanel.add(detailsLabel, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);

        // Listener para seleccionar un Pokémon
        pokemonTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = pokemonTable.getSelectedRow();
            if (selectedRow != -1) {
                String spriteUrl = (String) tableModel.getValueAt(selectedRow, 4);
                String details = String.format("ID: %s | Nombre: %s | Altura: %s | Peso: %s",
                        tableModel.getValueAt(selectedRow, 0),
                        tableModel.getValueAt(selectedRow, 1),
                        tableModel.getValueAt(selectedRow, 2),
                        tableModel.getValueAt(selectedRow, 3));
                showSprite(spriteUrl);
                detailsLabel.setText(details);
            }
        });
    }

    private void loadPokemonByGeneration(String generation) {
        // Limpiar la tabla
        tableModel.setRowCount(0);

        // Obtener los Pokémon de la generación seleccionada
        List<Pokemon> pokemonList = pokemonRepository.findByGeneration(generation);

        // Agregar los Pokémon a la tabla
        for (Pokemon pokemon : pokemonList) {
            tableModel.addRow(new Object[]{
                    pokemon.getId(),
                    pokemon.getName(),
                    pokemon.getHeight(),
                    pokemon.getWeight(),
                    pokemon.getSprite()
            });
        }
    }

    private void showSprite(String spriteUrl) {
        try {
            if (spriteUrl != null && !spriteUrl.isEmpty()) {
                ImageIcon spriteIcon = new ImageIcon(new URL(spriteUrl));
                Image scaledImage = spriteIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
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
