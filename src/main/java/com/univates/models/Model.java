package com.univates.models;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Model<T>
{
    private static Connection connection;
    private static boolean    show_sql = false;
    private static int        selected_db = 1;
    
    public static final int DB_POSTGRES = 1;
    public static final int DB_SQLITE   = 2;
    
    public Model() 
    {
        try 
        {
            if(Model.connection == null)
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

    protected int getNextId() 
    {
        String sql = "SELECT MAX(id) FROM " + this.getEntidade();

        try 
        {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) 
            {
                return rs.getInt(1) + 1;
            } 
            else 
            {
                return 1;
            }

        } 
        catch (SQLException e) 
        {
            throw new RuntimeException("Erro ao buscar o próximo id disponível.");
        }
    }
    
    public void store( int id ) 
    {
        if ( this.getObjetoById(id) == null ) 
        {
            this.inserirRegistro();
        } 
        else 
        {
            this.atualizarRegistro();
        }
    }

    protected Object getObjetoById( int id )
    {
        String sql = "SELECT * FROM " + this.getEntidade() + " WHERE id = ?";
        
        try
        {
            PreparedStatement prepared_statement = Model.connection.prepareStatement(sql);
            
            prepared_statement.setInt(1, id);
         
            printQuery(String.valueOf(prepared_statement));
            
            ResultSet resultSet = prepared_statement.executeQuery();
            
            return this.getObjectsFromResult(resultSet).get(0);
        }
        catch (Exception e) 
        {
            System.out.println(e.getMessage());
            return null;
        }
    }

    protected T getObjecto( ArrayList<Filtro> filtros )
    {
        try 
        {            
            return this.getObjetos(filtros).get(0);
        } 
        catch (Exception e) 
        {
            return null;
        }
    }

    protected ArrayList<T> getObjetos( ArrayList<Filtro> filtros )
    {
        String sql = "SELECT * FROM " + this.getEntidade() + " WHERE ";
        
        for (Filtro filtro : filtros) 
        {
            sql += filtro.getQuery() + " AND ";   
        }
        
        sql = sql.substring(0, sql.length() - 5);
        
        printQuery(sql);
        
        try
        {
            PreparedStatement prepared_statement = Model.connection.prepareStatement(sql);
            
            ResultSet resultSet = prepared_statement.executeQuery();
            
            return this.getObjectsFromResult(resultSet);
                
        }
        catch (Exception e) 
        {
            // throw new RuntimeException("Objeto não encontrado");
            return null;
        }
    }
    
    protected ArrayList<T> getObjetos( ArrayList<Filtro> filtros, String orderBy )
    {
        String sql = "SELECT * FROM " + this.getEntidade() + " WHERE ";
        
        for (Filtro filtro : filtros) 
        {
            sql += filtro.getQuery() + " AND ";   
        }
        
        sql = sql.substring(0, sql.length() - 5);
        
        sql += " ORDER BY " + orderBy;
        
        printQuery(sql);
        
        try
        {
            PreparedStatement prepared_statement = Model.connection.prepareStatement(sql);
            
            ResultSet resultSet = prepared_statement.executeQuery();
            
            return this.getObjectsFromResult(resultSet);
                
        }
        catch (Exception e) 
        {
            // throw new RuntimeException("Objeto não encontrado");
            return null;
        }
    }
    
    private ArrayList<T> getObjectsFromResult( ResultSet rs )
    {
        try 
        {
            ArrayList<T> objetos = new ArrayList<T>();
            
            ResultSetMetaData rsmd = rs.getMetaData();
            
            String[] colunas = this.getColunas().split(", ");
            while (rs.next()) 
            {
                objetos.add(this.setObject(rs, rsmd, colunas));
            }
            
            return objetos;
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private T setObject( ResultSet rs, ResultSetMetaData rsmd, String[] colunas ) throws Exception 
    {
        T objeto = (T) this.getClass().getDeclaredConstructor().newInstance();

        for (int i = 0; i < colunas.length; i++) 
        {
            String coluna           = colunas[i];
            String tipo             = rsmd.getColumnTypeName(i + 1);
            String setterMethodName = "set" + coluna.substring(0, 1).toUpperCase() + coluna.substring(1);
            
            switch (tipo.toLowerCase()) 
            {
                case "int4":
                case "int8":
                case "serial":
                case "bigserial":
                case "integer":
                    objeto.getClass().getDeclaredMethod(setterMethodName, int.class).invoke(objeto, rs.getInt(coluna));
                break;
                case "float4":
                case "float8":
                case "numeric":
                case "real":
                case "double precision":
                    objeto.getClass().getDeclaredMethod(setterMethodName, double.class).invoke(objeto, rs.getDouble(coluna));
                break;
                case "varchar":
                case "text":
                    objeto.getClass().getDeclaredMethod(setterMethodName, String.class).invoke(objeto, rs.getString(coluna));
                break;
                case "timestamp":
                    objeto.getClass().getDeclaredMethod(setterMethodName, Timestamp.class).invoke(objeto, rs.getTimestamp(coluna));
                break;
            }
        }      
        
        return objeto;
    }

    //TODO: implementar atualizarRegistro()
    private void atualizarRegistro()
    {
        System.out.println("Atualizando registro");
    }

    private void inserirRegistro() 
    {
        String colunas      = getColunas();
        String placeholders = getPlaceholders(colunas);
    
        String sql = "INSERT INTO " + getEntidade() + " ( " + colunas + " ) VALUES ( " + placeholders + " )";
    
        try 
        {
            PreparedStatement stmt = connection.prepareStatement(sql);
            setValores(stmt);
            System.out.println("aqui");
            System.out.println(stmt);
            stmt.execute();
            stmt.close();
        } 
        catch (SQLException e) 
        {
            throw new RuntimeException(e.getMessage());
        }
    }
    
    private String getEntidade()
    {
        return this.getClass().getSimpleName().toLowerCase();
    }
    
    private String getColunas() 
    {
        Class<?> clazz = this.getClass();
        Field[] fields = clazz.getDeclaredFields();
        
        String colunas = "";
        
        for (Field field : fields) 
        {
            field.setAccessible(true);
            
            String fieldName = field.getName();
            
            String getterMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            
            try 
            {
                Method getterMethod = clazz.getMethod(getterMethodName);
                
                if (getterMethod != null) 
                {
                    if (!colunas.isEmpty()) 
                    {
                        colunas += ", ";
                    }
                    
                    colunas += fieldName;
                }
            } 
            catch (NoSuchMethodException ignored) {}
        }
        
        return colunas;
    }
    
    private String getPlaceholders(String colunas) 
    {
        String[]      colunaArray  = colunas.split(",");
        StringBuilder placeholders = new StringBuilder();
    
        for (int i = 0; i < colunaArray.length; i++) 
        {
            if (placeholders.length() > 0) 
            {
                placeholders.append(", ");
            }
            
            placeholders.append("?");    
        }
        
        return placeholders.toString();
    }
    
    private void setValores( PreparedStatement stmt ) throws SQLException 
    {
        Class<?> clazz  = this.getClass();
        Field[]  fields = clazz.getDeclaredFields();
        
        int parameterIndex = 1;
    
        for (Field field : fields) 
        {
            field.setAccessible(true);
            Object value = null;
    
            String getterMethodName = "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
    
            try 
            {
                Method getterMethod = clazz.getMethod(getterMethodName);
                
                value = getterMethod.invoke(this);
                
                stmt.setObject(parameterIndex, value);
                
                parameterIndex++;
            } 
            catch (Exception ignored) {}
        }
    }

    public static boolean fecharConexao() 
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
        return Model.connection;
    }
    
    public static void setShowSql( boolean show_sql )
    {
        Model.show_sql = show_sql;
    }
    
    private void printQuery( String sql )
    {
        if (show_sql) 
        {
            System.out.println(sql);
        }
    }
    
    public static int setDb( int selected_db )
    {
        return Model.selected_db = selected_db;
    }
    
    private void setSelectedDb()
    {
        switch (Model.selected_db) 
        {
            case DB_POSTGRES:
                try 
                {
                    Class.forName("org.postgresql.Driver");
                    String url      = "jdbc:postgresql://localhost:5432/postgres";
                    String user     = "kauan";
                    String password = "senha";
                    Model.connection = DriverManager.getConnection(url, user, password);
                } 
                catch (Exception e) 
                {
                    e.printStackTrace();
                }
                return;
            case DB_SQLITE:
                try 
                {
                    Class.forName("org.sqlite.JDBC");
                    String url      = "jdbc:sqlite:src/main/java/com/univates/archives/db_pratica14.sqlite";
                    Model.connection = DriverManager.getConnection(url);
                } 
                catch (Exception e) 
                {
                    e.printStackTrace();
                }
                return;
        }
    }
}
