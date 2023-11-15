package com.univates.models;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.FileInputStream;


public class Dao 
{
    protected static Connection connection;
    protected static boolean    show_sql    = false;
    protected static int        selected_db = 1;
    
    public static final int DB_POSTGRES = 1;
    public static final int DB_SQLITE   = 2;

    public Dao()
    {
        try 
        {
            if(Dao.connection == null)
            {
                setSelectedDb();
            }
        }
        catch (Exception e) 
        {
            System.out.println("Erro ao conectar com o banco de dados.");
            System.out.println(e.getMessage());
        }
    }

    public static boolean closeConnection() 
    {
        try 
        {
            if (connection != null) 
            {
                connection.close();
            }
            return true;
        } 
        catch (SQLException e) 
        {
            // throw new RuntimeException("Erro ao fechar a conexão com o banco de dados.");
            return false;
        }
    }
    
    public static Connection getConnection()
    {
        return Dao.connection;
    }
    
    public static void setShowSql( boolean show_sql )
    {
        Dao.show_sql = show_sql;
    }
    
    protected void printQuery( String sql )
    {
        if (show_sql) 
        {
            System.out.println(sql);
        }
    }
    
    public static int setDb( int selected_db )
    {
        return Dao.selected_db = selected_db;
    }

    protected void setSelectedDb()
    {
        String url, user, password;
        try 
        {
            switch (Dao.selected_db) 
            {
                case DB_POSTGRES:
                    Properties  properties     = new Properties();
                    InputStream arquivo_config = new FileInputStream("src/main/resources/postgres.properties");
                    
                    properties.load(arquivo_config);
                    
                    Class.forName("org.postgresql.Driver");
                    
                    url      = "jdbc:postgresql://localhost:5432/postgres";
                    user     = properties.getProperty("usuario");
                    password = properties.getProperty("senha");
                    
                    Dao.connection = DriverManager.getConnection(url, user, password);
                break;
                case DB_SQLITE:
                    Class.forName("org.sqlite.JDBC");
                    
                    url = "jdbc:sqlite:src/main/java/com/univates/archives/db_pratica14.sqlite";
                    
                    Dao.connection = DriverManager.getConnection(url);
                break;
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

}
