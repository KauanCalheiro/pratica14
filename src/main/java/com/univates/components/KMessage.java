package com.univates.components;

import javax.swing.JOptionPane;

public class KMessage {
    
    
    public static void errorMessage( String message )
    {
        JOptionPane.showMessageDialog(null, message, "Erro", JOptionPane.ERROR_MESSAGE);
    }
    
    public static void infoMessage( String message )
    {
        JOptionPane.showMessageDialog(null, message, "Informação", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void warningMessage( String message )
    {
        JOptionPane.showMessageDialog(null, message, "Aviso", JOptionPane.WARNING_MESSAGE);
    }
    
    public static boolean questionMessage(String message) 
    {
        Object[] options = {"Sim", "Não"};
        
        int resposta = JOptionPane.showOptionDialog(
            null,
            message,
            "Pergunta",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        );
    
        return (resposta == JOptionPane.YES_OPTION);
    }
}
