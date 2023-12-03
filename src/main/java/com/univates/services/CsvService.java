package com.univates.services;

import com.univates.models.Csv;
import com.univates.models.Transacao;
import com.univates.models.Usuario;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;


public class CsvService 
{
    public static Csv modeloCsv( String path_to_store_file, String nome ) throws Exception
    {
        Csv csv = new Csv( path_to_store_file + "/" + nome + ".csv");
        
        ArrayList<String> colunas = new ArrayList<>();
            
        colunas.add("Data");
        colunas.add("Valor");
            
        csv.escreveLinha( colunas, false );

        return csv;        
    }
    
    public static Csv exportDadosByUser( String path_to_store_file, Usuario user ) throws Exception
    {
        Csv csv = modeloCsv(path_to_store_file, user.getNome() ); 
        
        Transacao model_transacao = new Transacao();
        
        ArrayList<Transacao> transacoes = model_transacao.getTrancoesByRefUsuario( user.getId() );
        
        for( Transacao transacao : transacoes )
        {
            ArrayList<String> linha = new ArrayList<>();
            
            linha.add( String.valueOf( transacao.getDataFormatada( "dd/MM/yyyy" ) ) );
            linha.add( String.valueOf( transacao.getValor() ) );
            
            csv.escreveLinha( linha, true );
        }

        return csv;        
    }
    
    public static void importDados( String file_path, boolean erase_old, Usuario user )
    {
        Csv csv = new Csv( file_path );
        
        try
        {
            ArrayList<String> linhas_csv = csv.leArquivo();
            
            validateCsv( linhas_csv );
            
            if( erase_old )
            {
                Transacao model_transacao = new Transacao( );
                
                ArrayList<Transacao> transacoes = model_transacao.getTrancoesByRefUsuario( user.getId() );
                
                for ( Transacao transacao : transacoes ) 
                {
                    transacao.delete( transacao.getId() );
                }
            }
            
            for (String linha : linhas_csv) 
            {
                String[] colunas = linha.split(",");
                
                String col_date  = colunas[0].replace("\"", "");
                String col_value = colunas[1].replace("\"", "");
                
                int day   = Integer.parseInt( col_date.split("/")[0] );
                int month = Integer.parseInt( col_date.split("/")[1] );
                int year  = Integer.parseInt( col_date.split("/")[2] );
                
                Timestamp date = Timestamp.valueOf( LocalDateTime.of(year, month, day, 0, 0) );
                
                Transacao transacao = new Transacao( Double.parseDouble( col_value ), date, user, null );
                
                transacao.store();
            }
        }
        catch( Exception e )
        {
            e.printStackTrace();    
            throw new RuntimeException( e.getMessage() );
        }
    }
    
    private static void removeHeader( ArrayList<String> linhas_csv ) throws Exception
    {
        String header = linhas_csv.get(0);
        
        validateHeader( header );
        
        linhas_csv.remove(0);
    }
    
    private static void validateInput( String input, int collumn ) throws Exception
    {
        switch (collumn) 
        {
            case 0:
                validateDate( input );
                break;
            case 1:
                validateValue( input );
                break;
            default:
                throw new Exception("O arquivo CSV possui um formato inválido.");
        }
    }
    
    private static void validateDate( String date ) throws Exception
    {
        String[] itens = date.split("/");
        
        if (itens.length != 3) 
        {
            throw new Exception("A data do arquivo CSV possui um formato inválido.");
        }
        
        int day = Integer.parseInt( itens[0] );
        int month = Integer.parseInt( itens[1] );
        
        if ( day < 1 || day > 31 ) 
        {
            throw new Exception("O dia do arquivo CSV está fora do intervalo válido. Entrada: " + date);
        }
        
        if ( month < 1 || month > 12 ) 
        {
            throw new Exception("O mês do arquivo CSV está fora do intervalo válido. Entrada: " + date);
        }
    }
    
    private static void validateValue( String value ) throws Exception
    {
        try 
        {
            Double.parseDouble( value );
        } 
        catch (Exception e) 
        {
            throw new Exception("O valor do arquivo CSV possui um formato inválido. Entrada: " + value);
        }
    }
    
    private static void validateHeader( String header ) throws Exception
    {
        String[] itens = header.split(",");
        
        if (itens.length != 2) 
        {
            throw new Exception("O arquivo CSV não possui um cabeçalho válido.");
        }
        
        if ( !itens[0].equalsIgnoreCase("\"Data\"") ) 
        {
            throw new Exception("O arquivo CSV não possui um cabeçalho válido.");
        }
        
        if ( !itens[1].equalsIgnoreCase("\"Valor\"") ) 
        {
            throw new Exception("O arquivo CSV não possui um cabeçalho válido.");
        }
    }
    
    private static void validateCsv( ArrayList<String> linhas_csv ) throws Exception
    {
        removeHeader( linhas_csv );
            
        for (String linha : linhas_csv) 
        {
            String[] colunas = linha.split(",");
            
            for (int i = 0; i < colunas.length; i++) 
            {
                validateInput( colunas[i].replace("\"", ""), i );
            }
        }
    }
}
