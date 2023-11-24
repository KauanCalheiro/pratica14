package com.univates.components;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class KFileChooser 
{
    private JFileChooser            explorador_arquivos;
    private FileNameExtensionFilter filtro ;
    private File                    arquivo_selecionado;
    
    private int tipo_selecao;

    public KFileChooser() 
    {
        this.explorador_arquivos = new JFileChooser();
    }
    
    public void setFiltro( String descricao, String extensao )    
    {
        this.filtro = new FileNameExtensionFilter( descricao, extensao);
        
        this.explorador_arquivos.setFileFilter( this.filtro );
    }
    
    public void show()
    {
        this.tipo_selecao = this.explorador_arquivos.showOpenDialog( null );
    }
    
    public int getTipoSelecao()
    {
        return this.tipo_selecao;
    }
    
    public File getSelecao()
    {
        this.arquivo_selecionado = this.explorador_arquivos.getSelectedFile();
        return this.arquivo_selecionado;
    }
    
    public String getCaminhoSelecao()
    {
        return this.arquivo_selecionado.getAbsolutePath();
    }
    
    public void setTipoSelecao( int tipo_selecao )
    {
        this.explorador_arquivos.setFileSelectionMode(tipo_selecao);
    }
}
