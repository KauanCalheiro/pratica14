package com.univates.models;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;

public class Model<T> extends Dao
{
    public Model() 
    {
        super();
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
            this.insert();
        } 
        else 
        {
            this.update( id );
        }
    }

    private void update( int id )
    {
        String[] colunas = getColunas().split(", ");
        String[] placeholders = getPlaceholders(getColunas()).split(", ");
        
        String sql = "UPDATE " + getEntidade() + " SET ";
        
        for (int i = 0; i < colunas.length; i++) 
        {
            if (i > 0) 
            {
                sql += ", ";
            }
            sql += colunas[i] + " = " + placeholders[i];
        }
        
        sql += " WHERE id = " + id;
        
        try 
        {
            PreparedStatement stmt = connection.prepareStatement(sql);
            setPlaceholders(stmt);
            printQuery(stmt.getMetaData().toString());
            stmt.execute();
            stmt.close();
        } 
        catch (SQLException e) 
        {
            throw new RuntimeException(e.getMessage());
        }
        
    }

    private void insert() 
    {
        String colunas      = getColunas();
        String placeholders = getPlaceholders(colunas);
    
        String sql = "INSERT INTO " + getEntidade() + " ( " + colunas + " ) VALUES ( " + placeholders + " )";
    
        try 
        {
            PreparedStatement stmt = connection.prepareStatement(sql);
            setPlaceholders(stmt);
            printQuery(stmt.toString());
            stmt.execute();
            stmt.close();
        } 
        catch (SQLException e) 
        {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void delete( int id )
    {
        String sql = "DELETE FROM " + this.getEntidade() + " WHERE id = ?";
        
        try 
        {
            PreparedStatement stmt = connection.prepareStatement(sql);
            
            stmt.setInt(1, id);
            stmt.execute();
            stmt.close();
        } 
        catch (SQLException e) 
        {
            throw new RuntimeException(e.getMessage());
        }
    }
    
    public T getObjetoById( int id )
    {
        ArrayList<Filtro> filtros = new ArrayList<Filtro>();
        
        filtros.add( new Filtro("id", "=", String.valueOf(id)) );
        
        return this.getObjecto(filtros);
    }

    public T getObjecto( ArrayList<Filtro> filtros )
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

    public ArrayList<T> getObjetos( ArrayList<Filtro> filtros )
    {
        return this.getObjetos(filtros, null);
    }
    
    public ArrayList<T> getObjetos( ArrayList<Filtro> filtros, String orderBy )
    {
        String sql = "SELECT * FROM " + this.getEntidade() + " WHERE ";
        
        for (Filtro filtro : filtros) 
        {
            sql += filtro.getQuery() + " AND ";   
        }
        
        sql = sql.substring(0, sql.length() - 5);
        
        if (orderBy != null) 
        {
            sql += " ORDER BY " + orderBy;
        }
        
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
            String setterMethodName = formatMethodName( "set", coluna );
            
            Method setMethod = findSetterMethod( setterMethodName);

            if (setMethod != null) 
            {
                Class<?> parameterType = setMethod.getParameterTypes()[0];
    
                setMethod.invoke(objeto, getValue(rs, coluna, parameterType));
            }
        }      
        
        return objeto;
    }

    private Method findSetterMethod( String methodName ) 
    {
        for (java.lang.reflect.Method method : this.getClass().getDeclaredMethods()) {
            if (method.getName().equals(methodName) && method.getParameterCount() == 1) {
                return method;
            }
        }
        return null;
    }
    
    private Object getValue(ResultSet rs, String coluna, Class<?> parameterType) throws SQLException 
    {
        switch (parameterType.getSimpleName()) 
        {
            case "int":
                return rs.getInt(coluna);

            case "double":
                return rs.getDouble(coluna);

            case "String":
                return rs.getString(coluna);

            case "Timestamp":
                return rs.getTimestamp(coluna);

            case "boolean":
                return rs.getBoolean(coluna);

            default:
                throw new IllegalArgumentException("Paramentro não suportado: " + parameterType);
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
            
            String getterMethodName = formatMethodName("get", fieldName);
            
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
    
    private void setPlaceholders( PreparedStatement stmt ) throws SQLException 
    {
        Class<?> clazz  = this.getClass();
        Field[]  fields = clazz.getDeclaredFields();
        
        int parameterIndex = 1;
    
        for (Field field : fields) 
        {
            field.setAccessible(true);
            Object value = null;
    
            String getterMethodName = formatMethodName("get", field.getName());
    
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

    private String formatMethodName( String type, String methodName )
    {
        methodName = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
        String[] methodNameArray = methodName.split("_");

        StringBuilder formattedMethodName = new StringBuilder();

        for( int i = 0; i < methodNameArray.length; i++ ) 
        {
            String word = methodNameArray[i];

            if (i > 0) 
            {
                word = word.substring(0, 1).toUpperCase() + word.substring(1);
            }

            formattedMethodName.append(word);
        }

        return type + formattedMethodName.toString();
    }
    
}
