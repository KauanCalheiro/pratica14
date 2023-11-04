package com.univates.screens;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFrame;

public class TelaEdicao extends JFrame {
    private JButton botaoVoltar = new JButton("-");

    public TelaEdicao(){
        setSize(800, 500); 
        setTitle("Tela de Edição"); 
        setDefaultCloseOperation(EXIT_ON_CLOSE); 
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);

        botaoVoltar.setBounds(555, 300, 200, 20);
        botaoVoltar .setSize( botaoVoltar.getPreferredSize() );
        add(botaoVoltar);
        botaoVoltar.addActionListener(this::acaoVoltar);
    }

    private void acaoVoltar (ActionEvent actionEvent) 
    {
        //ajeitar
        TelaPrincipal tp = new TelaPrincipal();
        
        this.dispose();
        
        tp.setVisible(true);
    }
}
