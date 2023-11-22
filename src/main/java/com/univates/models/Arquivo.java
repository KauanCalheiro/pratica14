package com.univates.models;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Classe que representa um arquivo.
 */
public class Arquivo
{
    private String nome_arquivo;

    /**
     * Construtor da classe Arquivo.
     * 
     * @param nome_arquivo O nome do arquivo.
     */
    public Arquivo( String nome_arquivo )
    {
        this.nome_arquivo = "src/main/java/com/univates/archives/" + nome_arquivo ;
    }

    /**
     * Escreve as linhas fornecidas em um arquivo.
     * 
     * @param linhas             as linhas a serem escritas no arquivo
     * @param adicionar_conteudo indica se o conteúdo deve ser adicionado ao arquivo existente ou substituído
     * @return true se as linhas foram escritas com sucesso, false caso contrário
     * @throws Exception se ocorrer um erro ao escrever no arquivo
     */
    public boolean escreveArquivo( ArrayList<String> linhas, boolean adicionar_conteudo ) throws Exception
    {
        try
        {
            int ultima_linha = linhas.size() - 1;
            BufferedWriter writer = new BufferedWriter( new FileWriter( nome_arquivo, adicionar_conteudo ) );

            if (adicionar_conteudo && getQuantidadeLinhas() > 0)
            {
                writer.newLine();
            }

            for (int i = 0; i < linhas.size(); i++) 
            {
                writer.write(linhas.get(i));
                
                if( i != ultima_linha )
                {
                    writer.newLine();
                }                           
            }

            writer.close();

            return true;
        }
        catch (IOException e)
        {
            throw new Exception("Erro ao escrever no arquivo: " + e.getMessage());
        }
    }

    /**
     * Lê o conteúdo de um arquivo e retorna como uma lista de strings.
     * 
     * @return A lista de strings contendo o conteúdo do arquivo.
     * @throws Exception Se ocorrer um erro ao ler o arquivo.
     */
    public ArrayList<String> leArquivo() throws Exception 
    {
        try 
        {
            BufferedReader reader = new BufferedReader(new FileReader(nome_arquivo));
            String linha;
            ArrayList<String> conteudo = new ArrayList<>();

            while ((linha = reader.readLine()) != null) 
            {
                conteudo.add(linha);
            }

            reader.close();
            return conteudo;
        } 
        catch (IOException e) 
        {
            throw new Exception("Erro ao ler o arquivo: " + e.getMessage());
        }
    }

    /**
     * Retorna a linha especificada do arquivo.
     *
     * @param numero_da_linha O número da linha a ser retornada.
     * @return A linha especificada do arquivo.
     * @throws Exception Se ocorrer um erro ao ler o arquivo.
     */
    public String getLinha(int numero_da_linha) throws Exception
    {
        ArrayList<String> linhas = leArquivo();

        if ( numero_da_linha > linhas.size() )
        {
            return null;
        }

        return linhas.get(numero_da_linha - 1);
    }

    /**
     * Retorna a quantidade de linhas do arquivo.
     * 
     * @return A quantidade de linhas do arquivo.
     * @throws Exception Se ocorrer algum erro ao ler o arquivo.
     */
    public int getQuantidadeLinhas() throws Exception
    {
        int qtde_linhas         = leArquivo().size();
        boolean possui_conteudo = ! getLinha( 1 ).isEmpty(); 
        
        return possui_conteudo ? qtde_linhas : 0 ;
    }

    /**
     * Verifica se o arquivo possui conteúdo.
     * 
     * @return true se o arquivo possui conteúdo, caso contrário, retorna false.
     * @throws Exception se ocorrer algum erro durante a leitura do arquivo.
     */
    public boolean hasConteudo() throws Exception
    {
        return leArquivo().size() > 0;
    }
    
    /**
     * Retorna o nome do arquivo.
     *
     * @return o nome do arquivo.
     */
    public String getNomeArquivo()
    {
        return this.nome_arquivo;
    }
}