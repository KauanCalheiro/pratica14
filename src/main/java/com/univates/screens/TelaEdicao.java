package com.univates.screens;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFrame;

import com.univates.models.Transacao;

public class TelaEdicao extends JFrame 
{
    private JButton botaoVoltar = new JButton("Fechar");

    public TelaEdicao( Transacao transacao )
    {
        setSize(800, 500); 
        setTitle("Tela de Edição"); 
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); 
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
        this.dispose();
    }
}
