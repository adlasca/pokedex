package ec.edu.uce.pokedex.ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;

public class PokedexUI extends JFrame {
    private JButton Search;
    private JTextField Buscador;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;

    public PokedexUI() {
        Buscador.addInputMethodListener(new InputMethodListener() {
            @Override
            public void inputMethodTextChanged(InputMethodEvent event) {

            }

            @Override
            public void caretPositionChanged(InputMethodEvent event) {

            }
        });
        Search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }
}
