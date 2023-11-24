package com.univates.models;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Uma classe que representa um arquivo CSV.
 * Estende a classe Arquivo.
 */
public class Csv extends Arquivo
{
    /**
     * Constrói um objeto Csv com o nome de arquivo especificado.
     * O nome do arquivo é prefixado com "csv/".
     * 
     * @param nomeArquivo O nome do arquivo CSV.
     */
    public Csv( String nomeArquivo ) 
    {
        //  "src/main/java/com/univates/archives/csv/" +
        super( nomeArquivo );
    }
    
    /**
     * Escreve uma linha de itens no arquivo CSV.
     * 
     * @param lista_itens A lista de itens a serem escritos.
     * @return true se a linha foi escrita com sucesso, false caso contrário.
     * @throws Exception se ocorrer um erro ao escrever a linha.
     */
    public boolean escreveLinha( ArrayList<String> lista_itens, boolean append ) throws Exception 
    {
        try 
        {
            return super.escreveArquivo( formataListaItens( lista_itens ), append );
        } 
        catch ( Exception e )
        {
            throw new Exception( e.getMessage() );
        }
    }
    
    /**
     * Formata a lista de itens em uma única linha de CSV.
     * 
     * @param lista_itens Lista de itens a serem formatados.
     * @return A lista de itens formatados.
     */
    public ArrayList<String> formataListaItens( ArrayList<String> lista_itens )
    {
        String linha = lista_itens.stream()
            .map(item -> "\"" + item + "\"")
            .collect(Collectors.joining(","));
            
        ArrayList<String> lista = new ArrayList<>();
        lista.add(linha);
            
        return lista;
    }
}
