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
    
    public static void questionMessage( String message )
    {
        JOptionPane.showMessageDialog(null, message, "Pergunta", JOptionPane.QUESTION_MESSAGE);
    }
}
